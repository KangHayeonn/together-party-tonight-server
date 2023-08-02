package webProject.togetherPartyTonight.domain.alert.dto.alertContent;

import lombok.Builder;
import lombok.Getter;
import webProject.togetherPartyTonight.domain.alert.dto.alertSocketContent.AlertDataSocketContent;
import webProject.togetherPartyTonight.domain.billing.entity.BillingHistory;
import webProject.togetherPartyTonight.domain.club.entity.Club;
import webProject.togetherPartyTonight.domain.member.entity.Member;

/**
 * Alert 안에 content 에 넣을 데이터
 * 정산 결제 데이터
 */
@Getter
@Builder
public class AlertBillingPayData extends AlertDataSocketContent {
    private Long clubId; // 내 모임방 아이디
    private String clubName; // 내 모임방 이름
    private Long memberId; // 결제(정산)한 사람 아이디
    private String nickName; // 결제(정산)한 사람 닉네임
    private Integer price; // 결제(정산)한 금액

    public static AlertBillingPayData toAlertBillingPayData(Club club, Member member, BillingHistory billingHistory) {
        return AlertBillingPayData.builder()
                .clubId(club.getClubId())
                .clubName(club.getClubName())
                .memberId(member.getId())
                .nickName(member.getNickname())
                .price(billingHistory.getPrice())
                .build();
    }
}
