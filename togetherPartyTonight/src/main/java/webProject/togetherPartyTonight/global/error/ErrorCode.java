package webProject.togetherPartyTonight.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 사용자 정의 예외를 선언
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    //400 BAD_REQUEST

    //404 NOT FOUND
    NOT_FOUND(HttpStatus.NOT_FOUND,"데이터가 존재하지 않습니다"),

    //409 CONFLICT
    ALREADY_EXIST (HttpStatus.CONFLICT,"같은 데이터가 이미 존재합니다"),

    //500 INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR (HttpStatus.INTERNAL_SERVER_ERROR,"서버 내부 에러입니다"),

    //403 FORBIDDEN
    FORBIDDEN(HttpStatus.FORBIDDEN,"해당 데이터에 대한 권한이 없습니다"),

    //401 UNAUTHORIZED
    UNAUTHORIZED (HttpStatus.UNAUTHORIZED, "인증이 필요합니다");

    /**
     * 필요한 에러코드 작성
     */


    private final HttpStatus httpStatus;
    private final String message;


}
