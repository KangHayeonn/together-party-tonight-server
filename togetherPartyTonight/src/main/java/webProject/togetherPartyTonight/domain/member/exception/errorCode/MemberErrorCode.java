package webProject.togetherPartyTonight.domain.member.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import webProject.togetherPartyTonight.global.error.ErrorInterface;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorInterface {


    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지않는 회원입니다."),

    INVALID_PASSWORD(HttpStatus.FORBIDDEN,"비밀번호가 틀립니다."),

    MEMBER_ALREADY_EXIST(HttpStatus.CONFLICT,"이미 존재하는 이메일입니다."),

    PASSWORD_DUPLICATED(HttpStatus.CONFLICT, "수정하는 비밀번호가 기존의 비밀번호와 같습니다."),

    NOT_COMPLY_PASSWORD_RULE(HttpStatus.BAD_REQUEST,"비밀번호를 입력하지 않았습니다."),

    FAILED_LOGOUT(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버의 문제로 로그아웃에 실패했습니다.");

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
