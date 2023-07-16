package webProject.togetherPartyTonight.domain.comment.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import webProject.togetherPartyTonight.global.error.ErrorInterface;

@Getter
@AllArgsConstructor
public enum CommentErrorCode implements ErrorInterface {
    INVALID_COMMENT_ID(HttpStatus.BAD_REQUEST,"유효하지 않은 댓글 id입니다."),
    SOCKET_MESSAGE_PUB_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "댓글 broadcast를 실패했습니다.");
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
