package webProject.togetherPartyTonight.domain.chat.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webProject.togetherPartyTonight.global.error.ErrorCode;

@Getter
@AllArgsConstructor
public class ChatException extends RuntimeException{
    private ErrorCode errorCode;
}
