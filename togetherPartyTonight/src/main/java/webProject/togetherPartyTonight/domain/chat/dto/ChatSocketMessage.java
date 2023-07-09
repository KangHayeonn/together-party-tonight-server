package webProject.togetherPartyTonight.domain.chat.dto;

import com.google.gson.Gson;
import webProject.togetherPartyTonight.global.websocket.socketMessage.CommonSocketResponseMessage;

public class ChatSocketMessage {

    private static final String messageType = "chat";

    static class ChatData{
        Long chatId;
        String chat;

        public ChatData(Long chatId, String chat) {
            this.chatId = chatId;
            this.chat = chat;
        }
    }

    public static String getMessage(String message, Long chatId){
        CommonSocketResponseMessage<ChatData> responseMessage = new CommonSocketResponseMessage<>();
        responseMessage.setType(messageType);
        ChatData chatData = new ChatData(chatId, message);
        responseMessage.setData(chatData);
        return new Gson().toJson(responseMessage);
    }
}
