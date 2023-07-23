package webProject.togetherPartyTonight.domain.alert.dto.alertContent;

import lombok.Builder;
import lombok.Getter;
import webProject.togetherPartyTonight.domain.club.entity.Club;

/**
 * Alert 안에 content 에 넣을 데이터
 * 모임 신청 응답 데이터
 */
@Getter
@Builder
public class AlertApproveData {
    private Long clubId; // 내가 신청한 모임 아이디
    private String clubName; // 내가 신청한 모임방 이름
    private Boolean approve;  // 모임 승인 여부

    public static AlertApproveData toAlertApproveData(Club club, Boolean approve) {
        return AlertApproveData.builder()
                .clubId(club.getClubId())
                .clubName(club.getClubName())
                .approve(approve)
                .build();
    }
}
