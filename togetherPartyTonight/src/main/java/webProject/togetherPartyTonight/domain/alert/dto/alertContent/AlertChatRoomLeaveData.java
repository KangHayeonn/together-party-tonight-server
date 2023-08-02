package webProject.togetherPartyTonight.domain.alert.dto.alertContent;

import lombok.Builder;
import lombok.Getter;
import webProject.togetherPartyTonight.domain.alert.dto.alertSocketContent.AlertDataSocketContent;
import webProject.togetherPartyTonight.domain.chat.entity.ChatRoom;
import webProject.togetherPartyTonight.domain.member.entity.Member;

/**
 * Alert 안에 content 에 넣을 데이터
 */
@Getter
@Builder
public class AlertChatRoomLeaveData extends AlertDataSocketContent {
    private Long chatRoomId;    //채팅방 아이디
    private String chatRoomName;    //나간 채팅방 이름
    private Long leaveMemberId;
    private String leaveMemberNickname;

    public static AlertChatRoomLeaveData toAlertChatRoomLeaveData(ChatRoom chatRoom, Member leaveMember) {
        return AlertChatRoomLeaveData.builder()
                .chatRoomId(chatRoom.getId())
                .chatRoomName(chatRoom.getChatRoomName(chatRoom.getOtherMember(leaveMember)))
                .leaveMemberId(leaveMember.getId())
                .leaveMemberNickname(leaveMember.getNickname())
                .build();
    }

}
