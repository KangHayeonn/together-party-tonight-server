package webProject.togetherPartyTonight.domain.club.service;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import webProject.togetherPartyTonight.domain.club.dto.request.ApproveRequestDto;
import webProject.togetherPartyTonight.domain.club.dto.request.DeleteClubAndSignupRequestDto;
import webProject.togetherPartyTonight.domain.club.dto.request.DeleteMyAppliedRequestDto;
import webProject.togetherPartyTonight.domain.club.dto.response.*;
import webProject.togetherPartyTonight.domain.club.entity.ApprovalState;
import webProject.togetherPartyTonight.domain.club.entity.Club;
import webProject.togetherPartyTonight.domain.club.entity.ClubMember;
import webProject.togetherPartyTonight.domain.club.entity.ClubSignup;
import webProject.togetherPartyTonight.domain.club.exception.ClubErrorCode;
import webProject.togetherPartyTonight.domain.club.exception.ClubException;
import webProject.togetherPartyTonight.domain.club.repository.ClubMemberRepository;
import webProject.togetherPartyTonight.domain.club.repository.ClubRepository;
import webProject.togetherPartyTonight.domain.club.repository.ClubSignupRepository;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.domain.member.exception.MemberException;
import webProject.togetherPartyTonight.domain.member.exception.errorCode.MemberErrorCode;
import webProject.togetherPartyTonight.domain.member.repository.MemberRepository;
import webProject.togetherPartyTonight.global.error.ErrorCode;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubJoinService {

    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    private final ClubSignupRepository clubSignupRepository;
    private final ClubMemberRepository clubMemberRepository;


    public void signup (DeleteClubAndSignupRequestDto requestDto) {
        Member member = memberRepository.getReferenceById(requestDto.getUserId());
        if (member == null) throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        Club club = clubRepository.getReferenceById(requestDto.getClubId());
        if (club==null) throw new ClubException(ClubErrorCode.INVALID_CLUB_ID);
        // TODO: 2023/06/17 JWT 내부 유저정보와 requestBody의 userId가 다르면 '권한 없음' exception

        Optional<ClubSignup> alreadySignup = clubSignupRepository.findByClubClubIdAndClubMemberId(requestDto.getClubId(), requestDto.getUserId());
        if(alreadySignup.isEmpty()) {
            ClubSignup clubSignup = requestDto.toClubSignup(club, member, club.getMaster());
            clubSignupRepository.save(clubSignup);
        }
        else {
            throw new ClubException(ClubErrorCode.ALREADY_SIGNUP);
        }
    }


    @Transactional
    public void deleteAppliedClub (DeleteMyAppliedRequestDto requestDto) {
        Long clubSignupId = requestDto.getClubSignupId();
        Long userId = requestDto.getUserId();

        memberRepository.findById(userId).orElseThrow(
                () -> new MemberException(ErrorCode.INVALID_MEMBER_ID)
        );

        // TODO: 2023/06/17 JWT 내부 유저정보와 userId가 다르면 '권한 없음' exception

        ClubSignup clubSignup = clubSignupRepository.findById(clubSignupId).orElseThrow(
                () -> new ClubException(ClubErrorCode.INVALID_CLUB_SIGNUP_ID)
        );

        if (clubSignup.getClubSignupApprovalState().equals(ApprovalState.PENDING)) {
            clubSignupRepository.deleteById(clubSignupId);
        }
        else if (clubSignup.getClubSignupApprovalState().equals(ApprovalState.APPROVE)){
            clubMemberRepository.deleteByClubClubIdAndMemberId(clubSignup.getClub().getClubId(), requestDto.getUserId());
            clubSignupRepository.deleteById(clubSignupId);
            checkIfRecruitComplete(clubSignup);
        }
        else {
            //
        }
    }


    @Transactional
    public void approve (ApproveRequestDto requestDto) {

        Long clubSignupId = requestDto.getClubSignupId();
        // TODO: 2023/06/17 JWT 내부 유저정보와 masterId가 다르면 '권한 없음' exception

        ClubSignup clubSignup = clubSignupRepository.findById(clubSignupId).orElseThrow(
                () -> new ClubException(ClubErrorCode.INVALID_CLUB_SIGNUP_ID)
        );
        if (clubSignup.getClubSignupApprovalState().equals(ApprovalState.PENDING)) {
            if (requestDto.getApprove()) {
                clubSignup.setClubSignupApprovalState(ApprovalState.APPROVE);
                clubSignup.setClubSignupApprovalDate(LocalDateTime.now());
                ClubMember clubMember = new ClubMember(clubSignup.getClub(), clubSignup.getClubMember());
                clubMemberRepository.save(clubMember);
                checkIfRecruitComplete(clubSignup);
            } else {
                clubSignup.setClubSignupApprovalState(ApprovalState.REFUSE);
            }
        }
        else {
            throw new ClubException(ClubErrorCode.ALREADY_APPROVED);
        }


    }

    //모임별 신청한 사람 조회
    // TODO: 2023/06/21  pending, refuse, approve, kicked 모두 보내고 있음 -> pending, approve 만?
    public ReceivedApplicationListDto getRequestListPerClub (Long userId, Long clubId) {
        Club club = clubRepository.findById(clubId).orElseThrow(
                () -> new ClubException(ClubErrorCode.INVALID_CLUB_ID)
        );

        memberRepository.findById(userId).orElseThrow(
                () -> new MemberException(ErrorCode.INVALID_MEMBER_ID)
        );

        if (club.getMaster().getId() != userId) throw new ClubException(ErrorCode.FORBIDDEN);

        List<ClubSignup> clubSignups = clubSignupRepository.findAllByClubClubId(clubId);

        List<ApplicationDto> list = clubSignups.stream()
                .map(c -> new ApplicationDto(c.getClubSignupId(), c.getClub().getClubId(), c.getClub().getClubName(),
                        c.getClubMember().getId(), c.getClubMember().getNickname(), c.getClubSignupDate(), c.getClubSignupApprovalState().toString(),
                        c.getCreatedDate(), c.getModifiedDate()))
                .collect(Collectors.toList());

        return new ReceivedApplicationListDto(list);
    }

    //내가 신청한 모임 조회
    //승인된거랑 거절된거 모두 보냄
    public MyAppliedClubListDto getAppliedList(Long userId) {
        memberRepository.findById(userId).orElseThrow(
                () -> new MemberException(ErrorCode.INVALID_MEMBER_ID)
        );

        // TODO: 2023/06/17 JWT 내부 유저정보와 userId가 다르면 '권한 없음' exception

        List<ClubSignup> signupMemberId = clubSignupRepository.findAllByClubMemberId(userId);

        List<ApplicationDto> list = signupMemberId.stream()
                .map(c -> new ApplicationDto(c.getClubSignupId(), c.getClub().getClubId(), c.getClub().getClubName(),
                        c.getClubMember().getId(), c.getClubMember().getNickname(), c.getClubSignupDate(), c.getClubSignupApprovalState().toString(),
                        c.getCreatedDate(), c.getModifiedDate()))
                .collect(Collectors.toList());

        return new MyAppliedClubListDto(list);
    }

    //내가 만든 모임 조회
    public MyOwnedClubListDto getOwnedList(Long userId) {
        memberRepository.findById(userId).orElseThrow(
                () -> new MemberException(ErrorCode.INVALID_MEMBER_ID)
        );

        // TODO: 2023/06/17 JWT 내부 유저정보와 userId가 다르면 '권한 없음' exception

        List<Club> clubs = clubRepository.findClubByMasterId(userId);

        List<MyOwnedClubDto> list = clubs.stream()
                .map(c -> new MyOwnedClubDto(c.getClubName(), c.getClubCategory(), c.getClubMaximum(),
                        c.getClubContent(), splitTags(c.getClubTags()), (float)c.getClubPoint().getX(), (float)c.getClubPoint().getY(),
                        c.getAddress(),c.getMeetingDate(),c.getCreatedDate(), c.getModifiedDate()))
                .collect(Collectors.toList());

        return new MyOwnedClubListDto(list);
    }

    public void kickout (DeleteMyAppliedRequestDto request) {
        Long clubSignupId = request.getClubSignupId();
        Long userId = request.getUserId(); //모임장 아이디
        ClubSignup signup = clubSignupRepository.findById(clubSignupId).orElseThrow(
                () -> new ClubException(ClubErrorCode.INVALID_CLUB_SIGNUP_ID)
        );
        if (signup.getClubMaster().getId() != userId) throw new ClubException(ErrorCode.INVALID_MEMBER_ID);

        signup.setClubSignupApprovalState(ApprovalState.KICKOUT);
        clubMemberRepository.deleteByMemberId(signup.getClubMember().getId());

    }

    public List<String> splitTags (String tags) {
        String[] split = tags.split(",");
        return Arrays.stream(split).collect(Collectors.toList());
    }

    public void checkIfRecruitComplete(ClubSignup clubSignup) {
        //현재 승인된 모임 인원이 모집 최대 인원에 도달했는지 여부 반환
        Club club = clubSignup.getClub();
        Integer memberCnt = clubMemberRepository.getMemberCnt(club.getClubId());
        Integer maximum = club.getClubMaximum();
        if (memberCnt==maximum) club.setClubState(false);
        else club.setClubState(true);
    }
}
