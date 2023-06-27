package webProject.togetherPartyTonight.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 사용자 정의 예외를 선언
 */
@Getter
@AllArgsConstructor
public enum ErrorCode implements ErrorInterface {

    //400 BAD_REQUEST
    INVALID_REQUEST_BODY_PARAMETER_TYPE(HttpStatus.BAD_REQUEST, "Request Body의 parameter type이 일치하지 않습니다"),
    DATE_PARSING_EXCEPTION(HttpStatus.BAD_REQUEST, "LocalDate형식에 맞게 보내야 합니다"),
    INVALID_IMAGE_EXTENSION(HttpStatus.BAD_REQUEST,"이미지 파일이 아닙니다. 파일 형식을 확인하세요."),
    INVALID_EXTENSION(HttpStatus.BAD_REQUEST, "정상적인 파일이 아닙니다. 확장자 명을 확인하세요"),
    INVALID_MEMBER_ID(HttpStatus.NOT_FOUND, "유효하지 않은 사용자 ID 입니다."),

    //409 CONFLICT
    ALREADY_EXIST(HttpStatus.CONFLICT, "같은 데이터가 이미 존재합니다"),

    //500 INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러입니다"),
    S3_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 문제로 인해 이미지 업로드에 실패하였습니다."),

    //403 FORBIDDEN
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다"),

    //401 UNAUTHORIZED
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다"),

    /**
     * 필요한 에러코드 작성
     */
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_TOKEN(HttpStatus.FORBIDDEN, "잘못된 토큰입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "멤버를 찾지 못했어요"),
    ;


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
