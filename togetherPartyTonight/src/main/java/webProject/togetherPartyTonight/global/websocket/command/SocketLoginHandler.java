package webProject.togetherPartyTonight.global.websocket.command;

import com.google.gson.Gson;
import webProject.togetherPartyTonight.global.websocket.WebSocketService;

public class SocketLoginHandler extends WebSocketHandler {

    class LoginCommand{
        String command;
        Login data;

        class Login{
            long userId;
        }
    }

    private WebSocketService owner;

    public SocketLoginHandler(WebSocketService webSocketService) {
        owner = webSocketService;
    }

    @Override
    public void execute(String json, String sessionId) {
        LoginCommand loginCommand = new Gson().fromJson(json, LoginCommand.class);
        long userId = loginCommand.data.userId;
        owner.login(userId, sessionId);
    }
}
