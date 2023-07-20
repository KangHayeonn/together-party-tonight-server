package webProject.togetherPartyTonight.domain.club.service;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import webProject.togetherPartyTonight.global.util.ClubUtils;

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
    private final ClubUtils clubUtils;


    public void signup (DeleteClubAndSignupRequestDto requestDto, Member member) {

        Club club = clubRepository.getReferenceById(requestDto.getClubId());
        if (club==null) throw new ClubException(ClubErrorCode.INVALID_CLUB_ID);

        if(club.getMaster().getId() == member.getId()) throw new ClubException(ClubErrorCode.CANNOT_SIGNUP_TO_MY_CLUB);

        Optional<ClubSignup> alreadySignup = clubSignupRepository.findByClubClubIdAndClubMemberId(requestDto.getClubId(), member.getId());
        if(alreadySignup.isEmpty()) {
            ClubSignup clubSignup = requestDto.toClubSignup(club, member, club.getMaster());
            clubSignupRepository.save(clubSignup);
        }
        else {
            throw new ClubException(ClubErrorCode.ALREADY_SIGNUP);
        }
    }


    @Transactional
    public void deleteAppliedClub (DeleteMyAppliedRequestDto requestDto, Member member) {
        Long clubSignupId = requestDto.getClubSignupId();

        ClubSignup clubSignup = clubSignupRepository.findById(clubSignupId).orElseThrow(
                () -> new ClubException(ClubErrorCode.INVALID_CLUB_SIGNUP_ID)
        );

        checkAuthority(clubSignup.getClubMember().getId(), member);

        if (clubSignup.getClubSignupApprovalState().equals(ApprovalState.PENDING)) {
            clubSignupRepository.deleteById(clubSignupId);
        }
        else if (clubSignup.getClubSignupApprovalState().equals(ApprovalState.APPROVE)){
            clubMemberRepository.deleteByClubClubIdAndMemberId(clubSignup.getClub().getClubId(), member.getId());
            clubSignupRepository.deleteById(clubSignupId);
            checkIfRecruitComplete(clubSignup);
        }
        else {
            throw new ClubException(ClubErrorCode.CANNOT_WITHDRAW);
        }
    }


    @Transactional
    public void approve (ApproveRequestDto requestDto, Member member) {

        Long clubSignupId = requestDto.getClubSignupId();

        ClubSignup clubSignup = clubSignupRepository.findById(clubSignupId).orElseThrow(
                () -> new ClubException(ClubErrorCode.INVALID_CLUB_SIGNUP_ID)
        );
        checkAuthority(clubSignup.getClubMaster().getId() ,member);

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
    public ReceivedApplicationListDto getRequestListPerClub (Long clubId, Member member) {
        Club club = clubRepository.findById(clubId).orElseThrow(
                () -> new ClubException(ClubErrorCode.INVALID_CLUB_ID)
        );

        checkAuthority(club.getMaster().getId(), member);

        List<ClubSignup> clubSignups = clubSignupRepository.findAllByClubClubId(clubId);

        List<ApplicationDto> list = clubSignups.stream()
                .map(c -> new ApplicationDto(c.getClubSignupId(), c.getClub().getClubId(), c.getClub().getClubName(),
                        c.getClubMember().getId(), c.getClubMember().getNickname(), c.getClubSignupDate(), c.getClubSignupApprovalState().toString(),
                        c.getCreatedDate(), c.getModifiedDate()))
                .collect(Collectors.toList());

        return new ReceivedApplicationListDto(list);
    }

    //내가 신청한 모임 조회
    public MyAppliedClubListDto getAppliedList(Member member, Pageable pageable, ApprovalState filter) {
        List<ApplicationDto> list = new ArrayList<>();
        Optional<Page<ClubSignup>> signupMemberId;

        if(filter.equals(ApprovalState.ALL)) signupMemberId = clubSignupRepository.findAllByClubMemberId(member.getId(), pageable);
        else signupMemberId= clubSignupRepository.findAllByClubMemberIdAndClubSignupApprovalStateLike(member.getId(),filter, pageable);

        if(signupMemberId.isPresent()) {
            list = signupMemberId.get().stream()
                    .map(c -> new ApplicationDto(c.getClubSignupId(), c.getClub().getClubId(), c.getClub().getClubName(),
                            c.getClubMaster().getId(), c.getClubMaster().getNickname(), c.getClubSignupDate(), c.getClubSignupApprovalState().toString(),
                            c.getCreatedDate(), c.getModifiedDate()))
                    .collect(Collectors.toList());
        }

        return new MyAppliedClubListDto(list,pageable.getPageNumber(), pageable.getPageSize(), signupMemberId.get().getTotalElements(), signupMemberId.get().getTotalPages());
    }

    //내가 만든 모임 조회
    public MyOwnedClubListDto getOwnedList(Member member, Pageable pageable,ApprovalState filter) {
        List<MyOwnedClubDto> list = new ArrayList<>();
        Optional<Page<Club>> clubs;

        if (filter.equals(ApprovalState.ALL)) clubs= clubRepository.findClubByMasterId(member.getId(), pageable);
        else if(filter.equals("RECRUITING")) clubs= clubRepository.findClubByMasterIdAndClubState(member.getId(), true, pageable);
        else clubs=clubRepository.findClubByMasterIdAndClubState(member.getId(), false, pageable);

        if (clubs.isPresent()) {
            list = clubs.get().stream()
                    .map(c -> new MyOwnedClubDto(c.getClubName(), c.getClubCategory(), c.getClubMaximum(),
                            c.getClubContent(), clubUtils.splitTags(c.getClubTags()), (float) c.getClubPoint().getX(), (float) c.getClubPoint().getY(),
                            c.getAddress(), c.getMeetingDate(), c.getImage(), c.getCreatedDate(), c.getModifiedDate()))
                    .collect(Collectors.toList());
        }
        return new MyOwnedClubListDto(list, pageable.getPageNumber(), pageable.getPageSize(), clubs.get().getTotalElements(), clubs.get().getTotalPages());
    }

    @Transactional
    public void kickout (DeleteMyAppliedRequestDto request, Member member) {
        Long clubSignupId = request.getClubSignupId();

        ClubSignup signup = clubSignupRepository.findById(clubSignupId).orElseThrow(
                () -> new ClubException(ClubErrorCode.INVALID_CLUB_SIGNUP_ID)
        );
        checkAuthority(signup.getClubMaster().getId() , member);

        signup.setClubSignupApprovalState(ApprovalState.KICKOUT);
        clubMemberRepository.deleteByMemberId(signup.getClubMember().getId());

    }



    public void checkIfRecruitComplete(ClubSignup clubSignup) {
        //현재 승인된 모임 인원이 모집 최대 인원에 도달했는지 여부 반환
        Club club = clubSignup.getClub();
        Integer memberCnt = clubMemberRepository.getMemberCnt(club.getClubId());
        Integer maximum = club.getClubMaximum();
        if (memberCnt==maximum) club.setClubState(false);
        else club.setClubState(true);
    }

    public void checkAuthority(Long memberId, Member member) {
        if(memberId != member.getId()) throw new ClubException(ErrorCode.FORBIDDEN);
    }
}
