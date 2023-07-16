package webProject.togetherPartyTonight.global.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebSocketHandler extends TextWebSocketHandler {

    private WebSocketService webSocketService;

    @Autowired
    public WebSocketHandler(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        webSocketService.addMember(session);
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        webSocketService.removeMember(session);
        super.afterConnectionClosed(session, status);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 클라이언트로부터 메시지를 받았을 때 동작할 로직 구현
        webSocketService.executeCommand(message.getPayload(),session.getId());
//        webSocketService.broadcastAll(receivedMessage, session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
    }
}

