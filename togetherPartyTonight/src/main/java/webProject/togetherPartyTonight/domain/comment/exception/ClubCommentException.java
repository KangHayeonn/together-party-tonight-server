package webProject.togetherPartyTonight.domain.comment.exception;

import lombok.Getter;
import webProject.togetherPartyTonight.global.error.ErrorCode;

@Getter
public class ClubCommentException extends RuntimeException{
    private ErrorCode errorCode;

    public ClubCommentException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode= errorCode;
    }
}
