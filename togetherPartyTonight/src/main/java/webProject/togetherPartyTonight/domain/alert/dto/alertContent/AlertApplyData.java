package webProject.togetherPartyTonight.domain.alert.dto.alertContent;

import lombok.Builder;
import lombok.Getter;
import webProject.togetherPartyTonight.domain.alert.dto.alertSocketContent.AlertDataSocketContent;
import webProject.togetherPartyTonight.domain.club.entity.Club;
import webProject.togetherPartyTonight.domain.member.entity.Member;


/**
 * Alert 안에 content 에 넣을 데이터
 */
@Getter
@Builder
public class AlertApplyData extends AlertDataSocketContent {
    private Long clubId; // 내 모임 아이디
    private String clubName; // 내 모임방 이름
    private Long memberId; // 신청한 사람 아이디
    private String nickName; // 신청한 사람 닉네임

    public static AlertApplyData toAlertApplyData(Club club, Member member){
        return AlertApplyData.builder()
                .clubId(club.getClubId())
                .clubName(club.getClubName())
                .memberId(member.getId())
                .nickName(member.getNickname())
                .build();
    }
}
