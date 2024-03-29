package webProject.togetherPartyTonight.domain.club.service;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import webProject.togetherPartyTonight.domain.alert.service.AlertService;
import webProject.togetherPartyTonight.domain.billing.entity.Billing;
import webProject.togetherPartyTonight.domain.billing.entity.BillingHistory;
import webProject.togetherPartyTonight.domain.billing.entity.BillingState;
import webProject.togetherPartyTonight.domain.club.dto.request.ApproveRequestDto;
import webProject.togetherPartyTonight.domain.club.dto.request.ClubStateFilterEnum;
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
import webProject.togetherPartyTonight.domain.comment.exception.CommentErrorCode;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.domain.member.exception.MemberException;
import webProject.togetherPartyTonight.domain.member.exception.errorCode.MemberErrorCode;
import webProject.togetherPartyTonight.domain.member.repository.MemberRepository;
import webProject.togetherPartyTonight.domain.review.repository.ReviewRepository;
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

    private final ReviewRepository reviewRepository;

    private final AlertService alertService;


    public void signup (DeleteClubAndSignupRequestDto requestDto, Member member) {

        Club club = clubRepository.findById(requestDto.getClubId()).orElseThrow(
                ()->new ClubException(ClubErrorCode.INVALID_CLUB_ID)
        ) ;

        if(club.getMaster().getId().equals(member.getId())) throw new ClubException(ClubErrorCode.CANNOT_SIGNUP_TO_MY_CLUB);
        if (club.getClubState()==false) throw new ClubException(ClubErrorCode.RECRUIT_FINISHED);

        Optional<ClubSignup> alreadySignup = clubSignupRepository.findByClubClubIdAndClubMemberId(requestDto.getClubId(), member.getId());
        if(alreadySignup.isEmpty()) {
            ClubSignup clubSignup = requestDto.toClubSignup(club, member, club.getMaster());
            clubSignupRepository.save(clubSignup);
            alertService.saveApplyAlertData(club,member); //모임장에게 알림 발송
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
                int memberCnt = clubMemberRepository.getMemberCnt(clubSignup.getClub().getClubId());
                int clubMaximum = clubSignup.getClub().getClubMaximum();
                if (clubMaximum<= memberCnt) {
                    throw new ClubException(ClubErrorCode.EXCEED_CLUB_MAXIMUM);
                }
                clubSignup.setClubSignupApprovalState(ApprovalState.APPROVE);
                clubSignup.setClubSignupApprovalDate(LocalDateTime.now());
                ClubMember clubMember = new ClubMember(clubSignup.getClub(), clubSignup.getClubMember());
                clubMemberRepository.save(clubMember);
                checkIfRecruitComplete(clubSignup);
            } else {
                clubSignup.setClubSignupApprovalState(ApprovalState.REFUSE);
            }
            alertService.saveApproveAlertData(clubSignup.getClub(), clubSignup.getClubMember(), requestDto.getApprove());
            //승낙/거절 시 모임 멤버에게 알림 발송
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

        List<ClubSignup> clubSignups = clubSignupRepository.findAllByClubClubId(clubId);
        boolean hasAuthority= false;

        for (ClubSignup cs : clubSignups) {
            if (cs.getClubMember().getId().equals(member.getId())) {
                hasAuthority= true;
                break;
            }
        }

        if (hasAuthority==false && !member.getId().equals(club.getMaster().getId())) {
            throw new ClubException(ErrorCode.FORBIDDEN);
        }

        List<ReceivedApplicationDto> list = clubSignups.stream()
                .map(c -> new ReceivedApplicationDto(c.getClubSignupId(), c.getClub().getClubId(), c.getClub().getClubName(),
                        c.getClubMember().getId(), c.getClubMember().getNickname(), c.getClubSignupDate(), c.getClubSignupApprovalState().toString(),
                        c.getClubMember().getProfileImage(),c.getClubMaster().getId(), c.getClubMaster().getProfileImage(),
                        c.getClubMaster().getNickname(), member.getId().equals(club.getMaster().getId()), String.valueOf(c.getClub().getMeetingDate()),
                        c.getCreatedDate(), c.getModifiedDate() ))
                .collect(Collectors.toList());

        return new ReceivedApplicationListDto(list);
    }

    //내가 신청한 모임 조회
    public MyAppliedClubListDto getAppliedList(Member member, Pageable pageable, String filter) {
        List<ApplicationDto> list = new ArrayList<>();
        Optional<Page<ClubSignup>> signupMemberId;

        if(filter.equals("ALL")) signupMemberId = clubSignupRepository.findAllByClubMemberId(member.getId(), pageable);
        else if((filter.equals("PENDING")) ||(filter.equals("APPROVE")) || (filter.equals("REFUSE")) || (filter.equals("KICKOUT"))){
            signupMemberId= clubSignupRepository.findAllByClubMemberIdAndClubSignupApprovalStateLike(member.getId(), ApprovalState.valueOf(filter), pageable);
        }
        else if (filter.equals("NO_REQUEST"))
            signupMemberId = clubSignupRepository.findByClubMemberIdAndBilling(member.getId(), pageable);
        else if(filter.equals("WAIT") || filter.equals("COMPLETED"))
            signupMemberId = clubSignupRepository.findByClubMemberIdAndBillingWait(member.getId(),filter,pageable);
        else throw new ClubException(ClubErrorCode.INVALID_FILTER);

        if(signupMemberId.isPresent()) {

            for (ClubSignup clubSignup : signupMemberId.get()) {
                int appliedCount = clubMemberRepository.getMemberCnt(clubSignup.getClub().getClubId());
                List<String> splitTags = clubUtils.splitTags(clubSignup.getClub().getClubTags());
                String billingState = getBillingState(clubSignup);

                Boolean isReviewWritten;

                if (reviewRepository.findByClubClubIdAndMemberId(clubSignup.getClub().getClubId(), clubSignup.getClubMember().getId()).isPresent())
                    isReviewWritten=true;
                else isReviewWritten=false;

                list.add(ApplicationDto.builder()
                        .clubSignupId(clubSignup.getClubSignupId())
                        .clubId(clubSignup.getClub().getClubId())
                        .clubName(clubSignup.getClub().getClubName())
                        .memberId(clubSignup.getClubMaster().getId())
                        .createdDate(clubSignup.getCreatedDate())
                        .modifiedDate(clubSignup.getModifiedDate())
                        .appliedCount(appliedCount)
                        .clubTags(splitTags)
                        .isReviewWritten(isReviewWritten)
                        .meetingDate(String.valueOf(clubSignup.getClub().getMeetingDate()))
                        .clubMaximum(clubSignup.getClub().getClubMaximum())
                        .approvalStatus(String.valueOf(clubSignup.getClubSignupApprovalState()))
                        .nickName(clubSignup.getClub().getMaster().getNickname())
                        .signupDate(clubSignup.getClubSignupDate())
                        .clubState(clubSignup.getClub().getClubState())
                        .billingState(billingState)
                        .build());
            }
        }

        return new MyAppliedClubListDto(list,pageable.getPageNumber(), pageable.getPageSize(), signupMemberId.get().getTotalElements(), signupMemberId.get().getTotalPages());
    }

    //내가 만든 모임 조회
    public MyOwnedClubListDto getOwnedList(Long memberId, Pageable pageable, String filter) {
        List<MyOwnedClubDto> list = new ArrayList<>();
        Optional<Page<Club>> clubs;


        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new ClubException(MemberErrorCode.MEMBER_NOT_FOUND)
        );

        if (filter.equals("ALL")) clubs= clubRepository.findClubByMasterId(member.getId(), pageable);
        else if(filter.equals("RECRUITING")) clubs= clubRepository.findClubByMasterIdAndClubState(member.getId(), true, pageable);
        else if(filter.equals("RECRUIT_FINISHED")) clubs=clubRepository.findClubByMasterIdAndClubState(member.getId(), false, pageable);
        else if (filter.equals("BILLING_NOT_STARTED")) clubs=clubRepository.findClubByMasterIdAndBillingNotStart(member.getId(), pageable) ;
        else if (filter.equals("BILLING_IN_PROGRESS")) clubs = clubRepository.findClubByMasterIdAndBillingStart(member.getId(),pageable) ;
        else throw new ClubException(ClubErrorCode.INVALID_FILTER);

        if (clubs.isPresent()) {
            for (Club c : clubs.get()) {
                int appliedCount = clubMemberRepository.getMemberCnt(c.getClubId());
                Point clubPoint = c.getClubPoint();
                List<String> splitTags = clubUtils.splitTags(c.getClubTags());
                Billing billing = c.getBilling();
                Boolean billingRequest = false;
                if (billing != null) billingRequest = true;

                list.add(MyOwnedClubDto.builder()
                        .clubName(c.getClubName())
                        .clubId(c.getClubId())
                        .clubCategory(c.getClubCategory())
                        .clubContent(c.getClubContent())
                        .clubTags(splitTags)
                        .clubMaximum(c.getClubMaximum())
                        .address(c.getAddress())
                        .image(c.getImage())
                        .appliedCount(appliedCount)
                        .meetingDate(c.getMeetingDate())
                        .latitude((float) clubPoint.getX())
                        .longitude((float) clubPoint.getY())
                        .createdDate(c.getCreatedDate())
                        .modifiedDate(c.getModifiedDate())
                        .clubState(c.getClubState())
                        .billingRequest(billingRequest)
                        .build());

            }
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
        if (memberCnt.equals(maximum)) club.setClubState(false);
        else club.setClubState(true);
    }

    public void checkAuthority(Long memberId, Member member) {
        if(!memberId.equals(member.getId())) throw new ClubException(ErrorCode.FORBIDDEN);
    }

    public String getBillingState (ClubSignup clubSignup) {
        Member member = clubSignup.getClubMember();
        Club club = clubSignup.getClub();
        String result = "NO_REQUEST";
        Optional<ClubMember> optionalClubMember = clubMemberRepository.findByClubClubIdAndMemberId(club.getClubId(), member.getId());
        if (optionalClubMember.isPresent()) {
            BillingHistory billingHistory = optionalClubMember.get().getBillingHistory();
            if (billingHistory==null) result= "NO_REQUEST";
            else if (billingHistory.getBillingState().equals(BillingState.COMPLETED))
                result = "COMPLETED";
            else result = "WAIT";
        }
        return result;
    }
}
