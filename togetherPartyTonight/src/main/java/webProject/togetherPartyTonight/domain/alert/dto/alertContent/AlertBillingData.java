package webProject.togetherPartyTonight.domain.alert.dto.alertContent;

import lombok.Builder;
import lombok.Getter;
import webProject.togetherPartyTonight.domain.alert.dto.alertSocketContent.AlertDataSocketContent;
import webProject.togetherPartyTonight.domain.billing.entity.BillingHistory;
import webProject.togetherPartyTonight.domain.club.entity.Club;

/**
 * Alert 안에 content 에 넣을 데이터
 * 정산 요청 데이터
 */
@Getter
@Builder
public class AlertBillingData extends AlertDataSocketContent {
    private Long clubId;    //모임 아이디
    private String clubName;//모임이름
    private Long memberId; // 모임장 아이디
    private Long billingHistoryId; //정산아이디
    private Integer price; // 결제(정산) 요청된 금액

    public static AlertBillingData toAlertBillingData(Club club, BillingHistory billingHistory) {
        return AlertBillingData.builder()
                .clubId(club.getClubId())
                .clubName(club.getClubName())
                .memberId(club.getMaster().getId())
                .billingHistoryId(billingHistory.getId())
                .price(billingHistory.getPrice())
                .build();
    }
}
