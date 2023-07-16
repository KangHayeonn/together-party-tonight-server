package webProject.togetherPartyTonight.domain.review.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webProject.togetherPartyTonight.global.error.CommonException;
import webProject.togetherPartyTonight.global.error.ErrorCode;
import webProject.togetherPartyTonight.global.error.ErrorInterface;

@Getter
public class ReviewException extends CommonException {
    public ReviewException(ErrorInterface errorCode) {
        super(errorCode);
    }
}
