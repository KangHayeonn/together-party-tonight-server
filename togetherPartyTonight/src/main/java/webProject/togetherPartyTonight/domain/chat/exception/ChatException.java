package webProject.togetherPartyTonight.domain.chat.exception;

import lombok.Getter;
import webProject.togetherPartyTonight.global.error.CommonException;
import webProject.togetherPartyTonight.global.error.ErrorInterface;

@Getter
public class ChatException extends CommonException {

    public ChatException(ErrorInterface errorCode) {
        super(errorCode);
    }
}
