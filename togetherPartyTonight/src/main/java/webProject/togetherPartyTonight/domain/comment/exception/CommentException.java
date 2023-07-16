package webProject.togetherPartyTonight.domain.comment.exception;

import lombok.Getter;
import webProject.togetherPartyTonight.global.error.CommonException;
import webProject.togetherPartyTonight.global.error.ErrorCode;
import webProject.togetherPartyTonight.global.error.ErrorInterface;

@Getter
public class CommentException extends CommonException {

    public CommentException(ErrorInterface errorCode) {
        super(errorCode);
    }
}
