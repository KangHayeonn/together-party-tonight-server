package webProject.togetherPartyTonight.domain.member.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import webProject.togetherPartyTonight.global.error.ErrorInterface;

@Getter
@RequiredArgsConstructor
public enum TokenErrorCode implements ErrorInterface {

    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다.");

    /**
     * 필요한 에러코드 작성
     */


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
