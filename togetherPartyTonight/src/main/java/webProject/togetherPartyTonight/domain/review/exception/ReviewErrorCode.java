package webProject.togetherPartyTonight.domain.review.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import webProject.togetherPartyTonight.global.error.ErrorInterface;

@Getter
@AllArgsConstructor
public enum ReviewErrorCode implements ErrorInterface {

    INVALID_REVIEW_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 reviewId 입니다."),
    INVALID_REVIEW_DATE(HttpStatus.BAD_REQUEST, "모임이 끝난 이후에 리뷰를 작성할 수 있습니다."),
    IS_ALREADY_WRITTEN(HttpStatus.BAD_REQUEST, "이미 리뷰를 작성했습니다. 한 모임에는 리뷰를 한번만 작성할 수 있습니다.");


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
