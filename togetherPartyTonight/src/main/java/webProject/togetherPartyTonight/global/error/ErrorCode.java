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
    INVALID_REQUEST_BODY_PARAMETER_TYPE(HttpStatus.BAD_REQUEST,"Request Body의 parameter type이 일치하지 않습니다"),
    IS_ALREADY_WRITTEN (HttpStatus.BAD_REQUEST, "이미 리뷰를 작성했습니다. 한 모임에는 리뷰를 한번만 작성할 수 있습니다."),
    INVALID_MEETING_DATE(HttpStatus.BAD_REQUEST,"모임 날짜가 현재 날짜와 같거나 그 이후여야 합니다"),
    INVALID_REVIEW_DATE(HttpStatus.BAD_REQUEST, "모임이 끝난 이후에 리뷰를 작성할 수 있습니다."),
    DATE_PARSING_EXCEPTION(HttpStatus.BAD_REQUEST, "LocalDate형식에 맞게 보내야 합니다"),
    INVALID_CLUB_MAXIMUM(HttpStatus.BAD_REQUEST, "현재 멤버 수보다 모임 최대 인원이 적습니다. 더 높게 입력해야 합니다."),
    INVALID_REVIEW_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 reviewId 입니다."),
    INVALID_CLUB_ID (HttpStatus.BAD_REQUEST,"유효하지 않은 모임 ID 입니다."),
    INVALID_CLUB_SIGNUP_ID(HttpStatus.NOT_FOUND,"유효하지 않은 모임 가입 ID 입니다."),
    INVALID_MEMBER_ID(HttpStatus.NOT_FOUND,"유효하지 않은 사용자 ID 입니다."),

    //409 CONFLICT
    ALREADY_EXIST (HttpStatus.CONFLICT,"같은 데이터가 이미 존재합니다"),
    ALREADY_APPROVED (HttpStatus.CONFLICT,"이미 수락/거절한 사람입니다"),

    //500 INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR (HttpStatus.INTERNAL_SERVER_ERROR,"서버 내부 에러입니다"),
    S3_UPLOAD_FAIL (HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 문제로 인해 이미지 업로드에 실패하였습니다."),

    //403 FORBIDDEN
    FORBIDDEN(HttpStatus.FORBIDDEN,"권한이 없습니다"),

    //401 UNAUTHORIZED
    UNAUTHORIZED (HttpStatus.UNAUTHORIZED, "인증이 필요합니다"),

    /**
     * 필요한 에러코드 작성
     */
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_TOKEN(HttpStatus.FORBIDDEN, "잘못된 토큰입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"멤버를 찾지 못했어요");


    private final HttpStatus httpStatus;
    private final String message;


}
