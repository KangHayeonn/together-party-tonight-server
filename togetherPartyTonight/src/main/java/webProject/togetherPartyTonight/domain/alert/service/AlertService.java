package webProject.togetherPartyTonight.domain.alert.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import webProject.togetherPartyTonight.domain.alert.dto.AlertDto;
import webProject.togetherPartyTonight.domain.alert.dto.AlertListRequestDto;
import webProject.togetherPartyTonight.domain.alert.dto.AlertListResponseDto;
import webProject.togetherPartyTonight.domain.alert.dto.AlertUnreadCountDto;
import webProject.togetherPartyTonight.domain.alert.dto.alertContent.*;
import webProject.togetherPartyTonight.domain.alert.dto.alertSocketMessage.SocketApplyData;
import webProject.togetherPartyTonight.domain.alert.entity.Alert;
import webProject.togetherPartyTonight.domain.alert.exception.AlertException;
import webProject.togetherPartyTonight.domain.alert.repository.AlertRepository;
import webProject.togetherPartyTonight.domain.billing.entity.BillingHistory;
import webProject.togetherPartyTonight.domain.billing.exception.BillingException;
import webProject.togetherPartyTonight.domain.chat.entity.Chat;
import webProject.togetherPartyTonight.domain.chat.entity.ChatRoom;
import webProject.togetherPartyTonight.domain.club.entity.Club;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.domain.member.repository.MemberRepository;
import webProject.togetherPartyTonight.global.common.response.SingleResponse;
import webProject.togetherPartyTonight.global.common.service.ResponseService;
import webProject.togetherPartyTonight.global.websocket.WebSocketService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static webProject.togetherPartyTonight.domain.alert.entity.AlertType.*;
import static webProject.togetherPartyTonight.domain.alert.exception.AlertErrorCode.ALREADY_READ;
import static webProject.togetherPartyTonight.domain.alert.exception.AlertErrorCode.NO_ALERT;
import static webProject.togetherPartyTonight.domain.billing.exception.BillingErrorCode.MEMBER_NOT_FOUND;

@Service
@Slf4j
public class AlertService {

    private ResponseService responseService;
    private MemberRepository memberRepository;
    private AlertRepository alertRepository;
    private WebSocketService webSocketService;

    @Autowired
    public AlertService(ResponseService responseService, MemberRepository memberRepository, AlertRepository alertRepository, WebSocketService webSocketService) {
        this.responseService = responseService;
        this.memberRepository = memberRepository;
        this.alertRepository = alertRepository;
        this.webSocketService = webSocketService;
    }

    private static final int defaultSeq = -1;       //알림 조회 시 첫 조회 요청 seq


    //알림 리스트 반환. 페이징 처리
    public SingleResponse<AlertListResponseDto> getAlertList(AlertListRequestDto alertListRequestDto) {
        Member member = getMemberBySecurityContextHolder();

        List<Alert> alerts = getAlerts(member.getId(), alertListRequestDto.getLastSeq(), alertListRequestDto.getListCount(), alertListRequestDto.getIsAllOrNotRead());
        List<AlertDto> alertDtoList = new ArrayList<>();
        alerts.forEach(alert -> alertDtoList.add(AlertDto.toDto(alert)));

        return responseService.getSingleResponse(new AlertListResponseDto(alertDtoList));
    }

    //알림 읽기
    public SingleResponse<String> readAlert(long alertId) {
        Member member = getMemberBySecurityContextHolder();
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> {
                    log.error("[AlertService] readAlert alert doesn't exist alertId: {}, memberId: {}", alertId, member.getId());
                    throw new AlertException(NO_ALERT);
                });

        if (alert.isAlertCheckState()) {
            log.warn("[AlertService] readAlert alert already checked, alertId: {}, memberId: {}", alertId, member.getId());
            throw new AlertException(ALREADY_READ);
        }

        alert.setAlertCheckState(true);
        alertRepository.save(alert);
        return responseService.getSingleResponse("알림 읽기에 성공하였습니다.");
    }

    //알림 삭제
    public SingleResponse<String> deleteAlert(long alertId) {
        Member member = getMemberBySecurityContextHolder();
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> {
                    log.error("[AlertService] deleteAlert alert doesn't exist alertId: {}, memberId: {}", alertId, member.getId());
                    throw new AlertException(NO_ALERT);
                });

        alertRepository.delete(alert);
        return responseService.getSingleResponse("알림 삭제에 성공하였습니다.");
    }


    //알림 수 확인 TodoSm 나중에 캐시처리 꼭 하기
    public SingleResponse<AlertUnreadCountDto> getAlertCount() {
        Member member = getMemberBySecurityContextHolder();
        Integer unreadCount = alertRepository.countByMemberIdAndAlertCheckState(member.getId(), false);
        AlertUnreadCountDto alertUnReadCountDto = AlertUnreadCountDto.builder().alertUnreadCount(unreadCount).build();
        return responseService.getSingleResponse(alertUnReadCountDto);
    }

    //모임 신청 시 모임장에게 가는 알림 저장 및 소켓 발송 기능
    public void saveApplyAlertData(Club club, Member applicant) {
        AlertApplyData alertApplyData = AlertApplyData.toAlertApplyData(club, applicant);
        Alert alert = Alert.builder()
                .member(club.getMaster())
                .alertType(APPLY)
                .alertContent(new Gson().toJson(alertApplyData))
                .alertCheckState(false)
                .build();

        Alert saveAlert = alertRepository.save(alert);
        String socketMessage = SocketApplyData.getMessage(alertApplyData, APPLY.toString(), saveAlert);

        //소켓에도 발송
        webSocketService.sendUser(club.getMaster().getId(), socketMessage);
    }

    //모임 승락/거절 시 가는 알림 저장 및 소켓 발송 기능
    public void saveApproveAlertData(Club club, Member receiver, Boolean approve) {
        AlertApproveData alertApproveData = AlertApproveData.toAlertApproveData(club, approve);
        Alert alert = Alert.builder()
                .member(receiver)
                .alertType(APPROVE)
                .alertContent(new Gson().toJson(alertApproveData))
                .alertCheckState(false)
                .build();

        Alert saveAlert = alertRepository.save(alert);
        String socketMessage = SocketApplyData.getMessage(alertApproveData, APPROVE.toString(), saveAlert);

        //소켓에도 발송
        webSocketService.sendUser(receiver.getId(), socketMessage);
    }

    //정산 요청 시 가는 알림 저장 및 소켓 발송 기능
    public void savaBillingAlertData(BillingHistory billingHistory) {
        AlertBillingData alertBillingData = AlertBillingData.toAlertBillingData(billingHistory.getBilling().getClub(), billingHistory);
        Alert alert = Alert.builder()
                .member(billingHistory.getClubMember().getMember())
                .alertType(BILLING_REQUEST)
                .alertContent(new Gson().toJson(alertBillingData))
                .alertCheckState(false)
                .build();

        Alert saveAlert = alertRepository.save(alert);
        String socketMessage = SocketApplyData.getMessage(alertBillingData, BILLING_REQUEST.toString(), saveAlert);

        //소켓에도 발송
        webSocketService.sendUser(billingHistory.getClubMember().getMember().getId(), socketMessage);
    }

    //모임장에게 가는 모임원의 정산 결제 알림 저장 및 소켓 발송 기능
    public void saveBillingPayAlertData(Club club, Member member, BillingHistory billingHistory) {
        AlertBillingPayData alertBillingPayData = AlertBillingPayData.toAlertBillingPayData(club, member, billingHistory);
        Alert alert = Alert.builder()
                .member(club.getMaster())
                .alertType(BILLING_PAY)
                .alertContent(new Gson().toJson(alertBillingPayData))
                .alertCheckState(false)
                .build();

        Alert saveAlert = alertRepository.save(alert);
        String socketMessage = SocketApplyData.getMessage(alertBillingPayData, BILLING_PAY.toString(), saveAlert);

        //소켓에도 발송
        webSocketService.sendUser(billingHistory.getClubMember().getMember().getId(), socketMessage);
    }

    //채팅 알림 발송
    public void saveChattingAlertData(Chat chat, ChatRoom chatRoom, Member sender) {
        AlertChatData alertChatData = AlertChatData.toAlertChatData(chat, chatRoom, sender);
        Member otherMember = chatRoom.getOtherMember(sender);
        Alert alert = Alert.builder()
                .member(otherMember)
                .alertType(CHAT)
                .alertContent(new Gson().toJson(alertChatData))
                .alertCheckState(false)
                .build();

        Alert saveAlert = alertRepository.save(alert);
        String socketMessage = SocketApplyData.getMessage(alertChatData, CHAT.toString(), saveAlert);

        //소켓에도 발송
        webSocketService.sendUser(otherMember.getId(), socketMessage);
    }

    //채팅 나가기 알림
    public void saveLeaveChatRoomAlertDat(ChatRoom chatRoom, Member leaveMember) {
        AlertChatRoomLeaveData alertChatRoomLeaveData = AlertChatRoomLeaveData.toAlertChatRoomLeaveData(chatRoom, leaveMember);

        Member otherMember = chatRoom.getOtherMember(leaveMember);
        Alert alert = Alert.builder()
                .member(otherMember)
                .alertType(LEAVE_CHATROOM)
                .alertContent(new Gson().toJson(alertChatRoomLeaveData))
                .alertCheckState(false)
                .build();
        Alert saveAlert = alertRepository.save(alert);

        String socketMessage = SocketApplyData.getMessage(alertChatRoomLeaveData, LEAVE_CHATROOM.toString(), saveAlert);

        //소켓에도 발송
        webSocketService.sendUser(otherMember.getId(), socketMessage);
    }

    private List<Alert> getAlerts(long memberId, long lastSeq, int listCount, Boolean isAllOrNotRead) {

        Optional<List<Alert>> alerts;
        Pageable pageable = PageRequest.of(0, listCount);

        if (isFirstPage(lastSeq)) {
            if (isAllOrNotRead) {
                alerts = alertRepository.findByMemberIdOrderByIdDesc(memberId, pageable);
            } else {
                alerts = alertRepository.findByMemberIdAndAlertCheckStateOrderByIdDesc(memberId, false, pageable);
            }
        } else {
            if (isAllOrNotRead) {
                alerts = alertRepository.findByMemberIdAndIdLessThanOrderByIdDesc(memberId, lastSeq, pageable);
            } else {
                alerts = alertRepository.findByMemberIdAndIdLessThanAndAlertCheckStateOrderByIdDesc(memberId, lastSeq, false, pageable);
            }
        }
        return alerts.orElse(new ArrayList<>());
    }

    // Jwt 토큰으로 인증 완료된 Member 를 받아온다.
    private Member getMemberBySecurityContextHolder() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        return memberRepository.findById(Long.parseLong(username))
                .orElseThrow(() -> {
                    log.error("[AlertService] getMemberBySecurityContextHolder member not found, memberId: {}", username);
                    throw new BillingException(MEMBER_NOT_FOUND);
                });
    }

    //------------- 조건 함수
    private boolean isFirstPage(long latestSeq) {
        return latestSeq == defaultSeq;
    }
}
