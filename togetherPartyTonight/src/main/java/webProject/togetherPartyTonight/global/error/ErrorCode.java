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
    ALREADY_EXIST (HttpStatus.BAD_REQUEST,"같은 데이터가 이미 존재합니다"),
    NOT_FOUND(HttpStatus.NOT_FOUND,"데이터가 존재하지 않습니다"),
    INCORRECT_QUERY_REQUEST(HttpStatus.BAD_REQUEST,"잘못된 쿼리 요청입니다"),
    FORBIDDEN(HttpStatus.FORBIDDEN,"해당 데이터에 대한 권한이 없습니다"),
    UNAUTHORIZED (HttpStatus.UNAUTHORIZED, "인증이 필요합니다");


    private final HttpStatus httpStatus;
    private final String message;


}
