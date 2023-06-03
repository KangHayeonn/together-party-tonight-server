package webProject.togetherPartyTonight.domain.notification.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webProject.togetherPartyTonight.global.error.ErrorCode;

@Getter
@AllArgsConstructor
public class NotificationException extends RuntimeException{
    private ErrorCode errorCode;
}
