package webProject.togetherPartyTonight.domain.alert.dto.alertSocketContent;

import lombok.Builder;
import lombok.Getter;
import webProject.togetherPartyTonight.domain.alert.dto.alertContent.AlertBillingPayData;

@Getter
@Builder
public class AlertBillingPaySocketContent extends AlertDataSocketContent{
    private Long clubId; // 내 모임방 아이디
    private String clubName; // 내 모임방 이름
    private Long memberId; // 결제(정산)한 사람 아이디
    private String nickName; // 결제(정산)한 사람 닉네임
    private Integer price; // 결제(정산)한 금액

    public static AlertBillingPaySocketContent toAlertBillingPaySocketContent(AlertBillingPayData alertBillingPayData) {
        return AlertBillingPaySocketContent.builder()
                .clubId(alertBillingPayData.getClubId())
                .clubName(alertBillingPayData.getClubName())
                .memberId(alertBillingPayData.getMemberId())
                .nickName(alertBillingPayData.getNickName())
                .price(alertBillingPayData.getPrice())
                .build();
    }
}
