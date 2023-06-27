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

    /**
     * 모임 신청
     * @param requestDto clubId : 신청하는 모임, userId : 신청하는 사람
     */
    public void signup (DeleteClubAndSignupRequestDto requestDto) {
        Club club = clubRepository.getReferenceById(requestDto.getClubId());
        if (club==null) throw new ClubException(ClubErrorCode.INVALID_CLUB_ID);
        Member member = memberRepository.getReferenceById(requestDto.getUserId());
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

    /**
     * 모임 신청 취소
     * @param requestDto clubSignupId : 취소하려는 모임, userId : 취소하는 사람
     */

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
            //아직 수락/거절을 하지 않았을 경우
            if (requestDto.getApprove()) { //true 이면
                clubSignup.setClubSignupApprovalState(ApprovalState.APPROVE);
                //clubSignup 의 approvalState 를 APPROVE 로 변경
                clubSignup.setClubSignupApprovalDate(LocalDateTime.now());
                //clubSignupApprovalDate 를 현재 시각으로 update
                ClubMember clubMember = new ClubMember(clubSignup.getClub(), clubSignup.getClubMember());
                clubMemberRepository.save(clubMember);
                //ClubMember 에 추가
                checkIfRecruitComplete(clubSignup);
                //최대 모집 인원에 도달 했는지 확인하고 모집완료 상태로 변경
            } else {
                clubSignup.setClubSignupApprovalState(ApprovalState.REFUSE);
                //clubSignup 의 approvalState 를 REFUSE 로 변경
            }
        }
        else {
            //이미 수락/거절을 한 경우
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

        List<ApplicationDto> list = new ArrayList<>();
        for (ClubSignup c : clubSignups) {
            ApplicationDto applicationDto = ApplicationDto.builder()
                    .clubSignupId(c.getClubSignupId())
                    .clubId(c.getClub().getClubId())
                    .clubName(c.getClub().getClubName())
                    .clubId(c.getClub().getClubId())
                    .userId(c.getClubMember().getId())
                    .nickName(c.getClubMember().getNickname())
                    .signupDate(c.getClubSignupDate().toString())
                    .approvalStatus(c.getClubSignupApprovalState().name())
                    .build();

            list.add(applicationDto);
        }
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
        List<ApplicationDto> list = new ArrayList<>();
        for (ClubSignup c : signupMemberId) {
            ApplicationDto applicationDto = ApplicationDto.builder()
                    .clubSignupId(c.getClubSignupId())
                    .clubId(c.getClub().getClubId())
                    .clubName(c.getClub().getClubName())
                    .clubId(c.getClub().getClubId())
                    .userId(c.getClub().getMaster().getId())
                    .nickName(c.getClubMaster().getNickname())
                    .signupDate(c.getClubSignupDate().toString())
                    .approvalStatus(c.getClubSignupApprovalState().name())
                    .build();

            list.add(applicationDto);
        }
        return new MyAppliedClubListDto(list);
    }

    //내가 만든 모임 조회
    public MyOwnedClubListDto getOwnedList(Long userId) {
        memberRepository.findById(userId).orElseThrow(
                () -> new MemberException(ErrorCode.INVALID_MEMBER_ID)
        );

        // TODO: 2023/06/17 JWT 내부 유저정보와 userId가 다르면 '권한 없음' exception

        List<Club> clubs = clubRepository.findClubByMasterId(userId);
        List<MyOwnedClubDto> list = new ArrayList<>();
        for (Club c : clubs) {

            Point point = c.getClubPoint();
            List<String> tags = splitTags(c.getClubTags());
            MyOwnedClubDto myOwnedClubDto = MyOwnedClubDto.builder()
                    .clubName(c.getClubName())
                    .clubCategory(c.getClubCategory())
                    .clubMinimum(c.getClubMinimum())
                    .clubMaximum(c.getClubMaximum())
                    .clubContent(c.getClubContent())
                    .clubTags(tags)
                    .latitude((float) point.getX())
                    .longitude((float) point.getY())
                    .address(c.getAddress())
                    .meetingDate(String.valueOf(c.getMeetingDate()))
                    .build();

            list.add(myOwnedClubDto);
        }
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
        // 상태를 '강퇴'로 변경
        clubMemberRepository.deleteByMemberId(signup.getClubMember().getId());
        // 멤버 목록에서 삭제

    }

    public List<String> splitTags (String tags) {
        String[] split = tags.split(",");
        return Arrays.stream(split).collect(Collectors.toList());
    }

    public void checkIfRecruitComplete(ClubSignup clubSignup) {
        //현재 승인된 모임 인원이 모집 최대 인원에 도달했는지 여부 반환
        Club club = clubSignup.getClub();
        Integer memberCnt = clubMemberRepository.getMemberCnt(club.getClubId()); //현재 승인된 인원
        Integer maximum = club.getClubMaximum(); //모임의 최대인원
        if (memberCnt==maximum) club.setClubState(false); //모임이 꽉 찼으면 모집완료 상태로 변경
        else club.setClubState(true);
    }
}
