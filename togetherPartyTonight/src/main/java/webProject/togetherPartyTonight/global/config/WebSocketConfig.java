package webProject.togetherPartyTonight.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import webProject.togetherPartyTonight.domain.chat.handler.ChatWebSocketHandler;
import webProject.togetherPartyTonight.domain.chat.service.WebSocketService;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    WebSocketService webSocketService;

    public WebSocketConfig(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatWebSocketHandler(webSocketService), "/chat"); //채팅 웹소켓 핸들러 등록
        // registry.addHandler(new ChatWebSocketHandler(), "/notification");   // 알림용 웹 소켓 핸들러 등록
    }
}