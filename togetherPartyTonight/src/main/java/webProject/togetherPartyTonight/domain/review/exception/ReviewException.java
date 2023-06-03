package webProject.togetherPartyTonight.domain.review.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webProject.togetherPartyTonight.global.error.ErrorCode;

@Getter
@AllArgsConstructor
public class ReviewException extends RuntimeException{
    private ErrorCode errorCode;
}
