package webProject.togetherPartyTonight.domain.alert.dto.alertSocketContent;

import lombok.Builder;
import lombok.Getter;
import webProject.togetherPartyTonight.domain.alert.dto.alertContent.AlertApplyData;

@Getter
@Builder
public class AlertApplySocketContent extends AlertDataSocketContent{
    private Long alertId; //알림 아이디
    private Long clubId; // 내 모임 아이디
    private String clubName; // 내 모임방 이름
    private Long memberId; // 신청한 사람 아이디
    private String nickName; // 신청한 사람 닉네임

    public static AlertApplySocketContent toAlertApplySocketContent(AlertApplyData alertApplyData, Long alertId){
        return AlertApplySocketContent.builder()
                .alertId(alertId)
                .clubId(alertApplyData.getClubId())
                .clubName(alertApplyData.getClubName())
                .memberId(alertApplyData.getMemberId())
                .nickName(alertApplyData.getNickName())
                .build();
    }
}
