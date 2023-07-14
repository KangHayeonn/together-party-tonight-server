package webProject.togetherPartyTonight.domain.member.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import webProject.togetherPartyTonight.global.error.ErrorInterface;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorInterface {
    NOT_EMAIL_SEND(HttpStatus.INTERNAL_SERVER_ERROR,"이메일을 보내지 못했습니다."),
    NOT_EQUAL_AUTH_CODE(HttpStatus.FORBIDDEN, "인증번호가 틀립니다."),
    AUTH_TIME_OUT(HttpStatus.UNAUTHORIZED, "인증번호 입력시간이 끝났습니다.");

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
