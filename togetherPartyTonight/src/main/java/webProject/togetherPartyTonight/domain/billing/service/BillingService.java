package webProject.togetherPartyTonight.domain.billing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import webProject.togetherPartyTonight.domain.alert.service.AlertService;
import webProject.togetherPartyTonight.domain.billing.dto.*;
import webProject.togetherPartyTonight.domain.billing.entity.Billing;
import webProject.togetherPartyTonight.domain.billing.entity.BillingHistory;
import webProject.togetherPartyTonight.domain.billing.exception.BillingException;
import webProject.togetherPartyTonight.domain.billing.repository.BillingHistoryRepository;
import webProject.togetherPartyTonight.domain.billing.repository.BillingRepository;
import webProject.togetherPartyTonight.domain.club.entity.Club;
import webProject.togetherPartyTonight.domain.club.entity.ClubMember;
import webProject.togetherPartyTonight.domain.club.repository.ClubMemberRepository;
import webProject.togetherPartyTonight.domain.club.repository.ClubRepository;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.domain.member.repository.MemberRepository;
import webProject.togetherPartyTonight.global.common.response.SingleResponse;
import webProject.togetherPartyTonight.global.common.service.ResponseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static webProject.togetherPartyTonight.domain.billing.entity.BillingState.COMPLETED;
import static webProject.togetherPartyTonight.domain.billing.entity.BillingState.WAIT;
import static webProject.togetherPartyTonight.domain.billing.exception.BillingErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillingService {

    ResponseService responseService;
    MemberRepository memberRepository;
    BillingRepository billingRepository;
    BillingHistoryRepository billingHistoryRepository;
    ClubRepository clubRepository;
    ClubMemberRepository clubMemberRepository;
    AlertService alertService;

    @Autowired
    public BillingService(ResponseService responseService, MemberRepository memberRepository, BillingRepository billingRepository, BillingHistoryRepository billingHistoryRepository, ClubRepository clubRepository, ClubMemberRepository clubMemberRepository, AlertService alertService) {
        this.responseService = responseService;
        this.memberRepository = memberRepository;
        this.billingRepository = billingRepository;
        this.billingHistoryRepository = billingHistoryRepository;
        this.clubRepository = clubRepository;
        this.clubMemberRepository = clubMemberRepository;
        this.alertService = alertService;
    }

    public SingleResponse<CreateBillingResponseDto> createBilling(CreateBillingRequestDto createBillingRequestDto) {

        Member member = getMemberBySecurityContextHolder();
        Club club = clubRepository.findById(createBillingRequestDto.getClubId())
                .orElseThrow(() -> {
                    log.error("[BillingService] createBilling club not found clubId: {}", createBillingRequestDto.getClubId());
                    throw new BillingException(CLUB_NOT_FOUNT);
                });
        //모임장만이 정산 요청이 가능
        if (!isClubMaster(member, club)) {
            throw new BillingException(NOT_MASTER);
        }
        List<ClubMember> clubMembers = club.getClubMembers();
        int totalMember = clubMembers.size();

        if (totalMember < 1) {
            log.error("[BillingService] createBilling club member size < 1, totalMember: {}, clubId: {}, memberId: {}", totalMember, club.getClubId(), member.getId());
            throw new BillingException(BILLING_MINIMUM_MEMBERS_NOT_MET_ERROR);
        }

        Billing billing = Billing.builder()
                .club(club)
                .price(createBillingRequestDto.getPrice())
                .build();

        Billing saveBilling = billingRepository.save(billing);

        int divPrice = getDivPrice(saveBilling, totalMember);

        clubMembers.forEach(clubMember -> {
            webProject.togetherPartyTonight.domain.billing.entity.BillingHistory billinghistory = webProject.togetherPartyTonight.domain.billing.entity.BillingHistory.builder()
                    .billing(saveBilling)
                    .billingState(WAIT)
                    .clubMember(clubMember)
                    .price(divPrice)
                    .build();
            BillingHistory billingHistory = billingHistoryRepository.save(billinghistory);
            alertService.savaBillingAlertData(billingHistory);
        });

        CreateBillingResponseDto createBillingResponseDto = CreateBillingResponseDto.builder()
                .BillingId(saveBilling.getId())
                .build();

        return responseService.getSingleResponse(createBillingResponseDto);
    }

    public SingleResponse<ClubBillingHistoryResponseDto> getClubBillingHistory(ClubBillingHistoryRequestDto clubBillingHistoryRequestDto) {

        Club club = clubRepository.findById(clubBillingHistoryRequestDto.getClubId())
                .orElseThrow(() -> {
                    log.error("[BillingService] getClubBillingDetail club not found clubId: {}", clubBillingHistoryRequestDto.getClubId());
                    throw new BillingException(CLUB_NOT_FOUNT);
                });

        Member member = getMemberBySecurityContextHolder();

        //모임장이면 pass
        if (!club.getMaster().getId().equals(member.getId())) {
            //클럽 멤버의 요청이 아니라면 throw
            club.getClubMembers().stream().filter(clubMember -> clubMember.getMember().getId().equals(member.getId()))
                    .findAny()
                    .orElseThrow(() -> {
                        log.error("[BillingService] getClubBillingDetail member is not in club, clubId: {}, memberId: {}", clubBillingHistoryRequestDto.getClubId(), member.getId());
                        throw new BillingException(MEMBER_NOT_CLUB_MEMBER);
                    });
        }

        List<BillingHistoryDto> clubBillingHistoryList = new ArrayList<>();
        Optional<Billing> billingOptional = billingRepository.findByClubClubId(club.getClubId());
        if (billingOptional.isEmpty()) {
            return responseService.getSingleResponse(new ClubBillingHistoryResponseDto(clubBillingHistoryList));
        }

        Billing billing = billingOptional.get();
        List<BillingHistory> billingHistoryList = billingHistoryRepository.findByBillingId(billing.getId())
                .orElseThrow(() -> {
                    log.error("[BillingService] getClubBillingDetail billing history is empty clubId: {}, memberId: {}, BillingId: {}", clubBillingHistoryRequestDto.getClubId(), member.getId(), billing.getId());
                    throw new BillingException(BILLING_HISTORY_NOT_FOUNT);
                });


        billingHistoryList.forEach(billingHistory -> {
            Member billingMember = billingHistory.getClubMember().getMember();

            BillingHistoryDto clubBillingHistory = BillingHistoryDto.builder()
                    .id(billingHistory.getId())
                    .memberId(billingMember.getId())
                    .nickname(billingMember.getNickname())
                    .price(billingHistory.getPrice())
                    .billingState(billingHistory.getBillingState().getStateString())
                    .build();

            clubBillingHistoryList.add(clubBillingHistory);
        });

        return responseService.getSingleResponse(new ClubBillingHistoryResponseDto(clubBillingHistoryList));
    }

    public SingleResponse<MyBillingHistoryResponseDto> getMyBillingHistory() {

        Member member = getMemberBySecurityContextHolder();
        Optional<List<ClubMember>> clubMemberOptional = clubMemberRepository.findByMemberId(member.getId());
        List<BillingHistoryDto> billingHistoryDtoList = new ArrayList<>();

        if (clubMemberOptional.isEmpty()) {
            return responseService.getSingleResponse(new MyBillingHistoryResponseDto(billingHistoryDtoList));
        }
        List<ClubMember> clubMemberList = clubMemberOptional.get();

        clubMemberList.forEach(clubMember -> {
            billingHistoryRepository.findByClubMemberClubMemberId(clubMember.getClubMemberId())
                    .ifPresent(billingHistory->{
                        BillingHistoryDto billingHistoryDto = BillingHistoryDto.toDto(billingHistory);
                        billingHistoryDtoList.add(billingHistoryDto);
                    });
        });

        return responseService.getSingleResponse(new MyBillingHistoryResponseDto(billingHistoryDtoList));
    }

    public SingleResponse<BillingPaymentResponseDto> processPayment(BillingPaymentRequestDto billingPaymentRequestDto) {
        Member member = getMemberBySecurityContextHolder();
        BillingHistory billingHistory = billingHistoryRepository.findById(billingPaymentRequestDto.getBillingHistoryId())
                .orElseThrow(() -> {
                    log.error("[BillingService] processPayment billingHistory is null memberId: {}, billingHistoryId: {}", member.getId(), billingPaymentRequestDto.getBillingHistoryId());
                    throw new BillingException(BILLING_HISTORY_NOT_FOUNT);
                });

        if (!isBillingHistoryMemberSame(member, billingHistory)) {
            log.error("[BillingService] processPayment billingHistory member id different memberId: {}, billingHistoryId: {}", member.getId(), billingPaymentRequestDto.getBillingHistoryId());
            throw new BillingException(BILLING_HISTORY_MEMBER_DIFFERENT);
        }

        if (isAlreadyPayed(billingHistory)) {
            log.error("[BillingService] processPayment billingHistory already payed memberId: {}, billingHistoryId: {}", member.getId(), billingPaymentRequestDto.getBillingHistoryId());
            throw new BillingException(BILLING_HISTORY_ALREADY_PAYED);
        }

        billingHistory.setBillingState(COMPLETED);
        BillingHistory saveBillingHistory = billingHistoryRepository.save(billingHistory);

        alertService.saveBillingPayAlertData(saveBillingHistory.getBilling().getClub(), member, saveBillingHistory);

        return responseService.getSingleResponse(BillingPaymentResponseDto.toDto(saveBillingHistory));
    }

    // Jwt 토큰으로 인증 완료된 Member 를 받아온다.
    private Member getMemberBySecurityContextHolder() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        return memberRepository.findById(Long.parseLong(username))
                .orElseThrow(() -> {
                    log.error("[BillingService] getMemberBySecurityContextHolder member not found, memberId: {}", username);
                    throw new BillingException(MEMBER_NOT_FOUND);
                });
    }

    private int getDivPrice(Billing billing, int totalMember) {
        return (int) (Math.ceil(billing.getPrice() / (double) totalMember));
    }

    private boolean isClubMaster(Member member, Club club) {
        return Objects.equals(club.getMaster().getId(), member.getId());
    }
    private boolean isClubMemberMaster(ClubMember masterClubMember, ClubMember clubMember) {
        return clubMember.getClubMemberId().equals(masterClubMember.getClubMemberId());
    }

    private boolean isBillingHistoryMemberSame(Member member, BillingHistory billingHistory) {
        return billingHistory.getClubMember().getMember().getId().equals(member.getId());
    }
    private boolean isAlreadyPayed(BillingHistory billingHistory) {
        return billingHistory.getBillingState().getStateString().equals(COMPLETED.getStateString());
    }
}
