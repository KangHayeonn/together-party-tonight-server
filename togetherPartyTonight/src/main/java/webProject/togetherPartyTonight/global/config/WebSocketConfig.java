package webProject.togetherPartyTonight.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import webProject.togetherPartyTonight.global.websocket.WebSocketHandler;
import webProject.togetherPartyTonight.global.websocket.WebSocketService;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    WebSocketService webSocketService;

    public WebSocketConfig(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketHandler(webSocketService), "/chatting"); //웹소켓 핸들러 등록

        // registry.addHandler(new ChatWebSocketHandler(), "/notification");   // 알림용 웹 소켓 핸들러 등록 (추후 구현 예정)
    }
}