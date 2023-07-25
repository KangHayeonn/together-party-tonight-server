package webProject.togetherPartyTonight.domain.chat.dto;

import com.google.gson.Gson;
import webProject.togetherPartyTonight.domain.chat.entity.ChatRoom;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.global.websocket.socketMessage.CommonSocketResponseMessage;

public class ChatSocketMessage {

    private static final String messageType = "chat";

    static class ChatData {
        Long chatRoomId;
        Long chatId;
        String chat;
        Long senderMemberId;
        String senderNickname;

        public ChatData(Long chatRoomId, Long chatId, String chat, Long senderMemberId, String senderNickname) {
            this.chatRoomId = chatRoomId;
            this.chatId = chatId;
            this.chat = chat;
            this.senderMemberId = senderMemberId;
            this.senderNickname = senderNickname;
        }
    }

    public static String getMessage(String message, Long chatId, Member sender, ChatRoom chatRoom){
        CommonSocketResponseMessage<ChatData> responseMessage = new CommonSocketResponseMessage<>();
        responseMessage.setType(messageType);
        ChatData chatData = new ChatData(chatRoom.getId(), chatId, message, sender.getId(), sender.getNickname());
        responseMessage.setData(chatData);
        return new Gson().toJson(responseMessage);
    }
}
