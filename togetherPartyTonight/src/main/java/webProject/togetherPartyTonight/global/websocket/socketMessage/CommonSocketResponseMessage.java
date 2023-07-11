package webProject.togetherPartyTonight.global.websocket.socketMessage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonSocketResponseMessage<T> {
    String type;
    T data;
}
