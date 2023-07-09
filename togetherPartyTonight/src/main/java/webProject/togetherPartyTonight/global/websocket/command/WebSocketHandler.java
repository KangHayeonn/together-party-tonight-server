package webProject.togetherPartyTonight.global.websocket.command;

public abstract class WebSocketHandler {
    //핸들러의 실행함수
    abstract public void execute(String json, String sessionId);
}
