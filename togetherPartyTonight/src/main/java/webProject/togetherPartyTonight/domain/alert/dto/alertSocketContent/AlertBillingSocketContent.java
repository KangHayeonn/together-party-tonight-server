package webProject.togetherPartyTonight.domain.alert.dto.alertSocketContent;


import lombok.Builder;
import lombok.Getter;
import webProject.togetherPartyTonight.domain.alert.dto.alertContent.AlertBillingData;

@Getter
@Builder
public class AlertBillingSocketContent extends AlertDataSocketContent {
    private Long billingId; //정산 아이디
    private Long clubId;    //모임 아이디
    private String clubName;//모임이름
    private Long memberId; // 모임장 아이디
    private Long billingHistoryId; //정산아이디
    private Integer price; // 결제(정산) 요청된 금액

    public static AlertBillingSocketContent toAlertSocketBillingData(AlertBillingData alertBillingData, Long billingHistoryId) {
        return AlertBillingSocketContent.builder()
                .billingHistoryId(billingHistoryId)
                .clubId(alertBillingData.getClubId())
                .clubName(alertBillingData.getClubName())
                .memberId(alertBillingData.getMemberId())
                .billingHistoryId(alertBillingData.getBillingHistoryId())
                .price(alertBillingData.getPrice())
                .build();
    }
}
