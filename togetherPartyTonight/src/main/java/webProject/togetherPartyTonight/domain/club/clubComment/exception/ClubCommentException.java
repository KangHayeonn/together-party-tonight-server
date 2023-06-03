package webProject.togetherPartyTonight.domain.club.clubComment.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webProject.togetherPartyTonight.global.error.ErrorCode;

@Getter
@AllArgsConstructor
public class ClubCommentException extends RuntimeException{
    private ErrorCode errorCode;
}
