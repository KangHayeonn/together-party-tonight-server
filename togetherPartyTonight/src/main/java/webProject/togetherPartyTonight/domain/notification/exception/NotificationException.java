package webProject.togetherPartyTonight.domain.notification.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webProject.togetherPartyTonight.global.error.ErrorCode;

@Getter
public class NotificationException extends RuntimeException{
    private ErrorCode errorCode;

    public NotificationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode= errorCode;
    }
}
