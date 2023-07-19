package webProject.togetherPartyTonight.domain.alert.dto.alertSocketContent;

import lombok.Builder;
import lombok.Getter;
import webProject.togetherPartyTonight.domain.alert.dto.alertContent.AlertChatData;

@Getter
@Builder
public class AlertChatSocketContent extends AlertDataSocketContent{
    private Long alertId;   //알림아이디
    private Long chatId; //채팅기록 아이디
    private Long chatRoomId; // 채팅방 아이디
    private Long sendMemberId; // 채팅을 보낸 사람의 아이디
    private String nickName; // 채팅을 보낸 사람의 닉네임
    private String chatMsg; // 채팅 내용

    public static AlertChatSocketContent toAlertChatSocketContent(AlertChatData alertChatData, Long alertId) {
        return AlertChatSocketContent.builder()
                .alertId(alertId)
                .chatId(alertChatData.getChatId())
                .chatRoomId(alertChatData.getChatRoomId())
                .sendMemberId(alertChatData.getSendMemberId())
                .nickName(alertChatData.getNickName())
                .chatMsg(alertChatData.getChatMsg())
                .build();
    }

}
