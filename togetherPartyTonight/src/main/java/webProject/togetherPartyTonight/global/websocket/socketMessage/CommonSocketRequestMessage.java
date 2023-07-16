package webProject.togetherPartyTonight.global.websocket.socketMessage;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonSocketRequestMessage {
    String command;
    Object data;
}
