package webProject.togetherPartyTonight.domain.alert.dto.alertContent;

import lombok.Builder;
import lombok.Getter;
import webProject.togetherPartyTonight.domain.chat.entity.Chat;
import webProject.togetherPartyTonight.domain.chat.entity.ChatRoom;
import webProject.togetherPartyTonight.domain.member.entity.Member;

/**
 * Alert 안에 content 에 넣을 데이터
 */
@Getter
@Builder
public class AlertChatData {
    private Long chatId; //채팅기록 아이디
    private Long chatRoomId; // 채팅방 아이디
    private Long sendMemberId; // 채팅을 보낸 사람의 아이디
    private String nickName; // 채팅을 보낸 사람의 닉네임
    private String chatMsg; // 채팅 내용

    public static AlertChatData toAlertChatData(Chat chat, ChatRoom chatRoom, Member sender) {
        return AlertChatData.builder()
                .chatId(chat.getId())
                .chatRoomId(chatRoom.getId())
                .sendMemberId(sender.getId())
                .nickName(sender.getNickname())
                .chatMsg(chat.getChatMsg())
                .build();
    }
}
