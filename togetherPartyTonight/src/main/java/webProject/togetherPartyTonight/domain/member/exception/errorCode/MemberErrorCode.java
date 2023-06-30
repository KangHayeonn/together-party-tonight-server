package webProject.togetherPartyTonight.domain.member.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import webProject.togetherPartyTonight.global.error.ErrorInterface;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorInterface {


    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "일치하는 이메일이 없습니다."),

    INVALID_PASSWORD(HttpStatus.FORBIDDEN,"비밀번호가 틀립니다.");

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
