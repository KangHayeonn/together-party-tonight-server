package webProject.togetherPartyTonight.domain.review.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webProject.togetherPartyTonight.global.error.ErrorCode;

@Getter
public class ReviewException extends RuntimeException{
    private ErrorCode errorCode;

    public ReviewException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode= errorCode;
    }
}
