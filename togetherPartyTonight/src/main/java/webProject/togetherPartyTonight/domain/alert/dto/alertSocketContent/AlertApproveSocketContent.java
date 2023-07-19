package webProject.togetherPartyTonight.domain.alert.dto.alertSocketContent;

import lombok.Builder;
import lombok.Getter;
import webProject.togetherPartyTonight.domain.alert.dto.alertContent.AlertApplyData;
import webProject.togetherPartyTonight.domain.alert.dto.alertContent.AlertApproveData;

@Getter
@Builder
public class AlertApproveSocketContent extends AlertDataSocketContent{
    private Long alertId; //알림 아이디
    private Long clubId; // 내가 신청한 모임 아이디
    private String clubName; // 내가 신청한 모임방 이름
    private Boolean approve;  // 모임 승인 여부

    public static AlertApproveSocketContent toAlertApproveSocketContent(AlertApproveData alertApproveData, Long alertId){
        return AlertApproveSocketContent.builder()
                .alertId(alertId)
                .clubId(alertApproveData.getClubId())
                .clubName(alertApproveData.getClubName())
                .approve(alertApproveData.getApprove())
                .build();
    }
}
