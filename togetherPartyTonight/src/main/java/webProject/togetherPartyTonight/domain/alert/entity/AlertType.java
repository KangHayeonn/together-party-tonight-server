package webProject.togetherPartyTonight.domain.alert.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AlertType {

    APPLY("APPLY"),  //모임 신청 요청
    APPROVE("APPROVE"),        //모임 승인 여부
    BILLING_REQUEST("BILLING_REQUEST"), //정산요청
    BILLING_PAY("BILLING_PAY"),     //정산 결제 알림
    CHAT("CHAT"),       //채팅 알림
    ;


    private String alertType;
}
