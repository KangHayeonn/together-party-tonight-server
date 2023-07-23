package webProject.togetherPartyTonight.domain.alert.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import webProject.togetherPartyTonight.global.error.ErrorInterface;

@Getter
@AllArgsConstructor
public enum AlertErrorCode implements ErrorInterface {


    //400 BAD_REQUEST
    NO_ALERT(HttpStatus.BAD_REQUEST, "알림이 없습니다"),
    ALREADY_READ(HttpStatus.BAD_REQUEST, "이미 읽은 알림입니다")
    ;

    //409 CONFLICT



    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public int getStatusCode() {
        return httpStatus.value();
    }

    @Override
    public String getErrorMessage() {
        return message;
    }
}
