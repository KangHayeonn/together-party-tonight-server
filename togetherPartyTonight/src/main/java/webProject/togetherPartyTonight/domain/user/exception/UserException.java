package webProject.togetherPartyTonight.domain.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webProject.togetherPartyTonight.global.error.ErrorCode;

@Getter
@AllArgsConstructor
public class UserException extends RuntimeException{
    private ErrorCode errorCode;

}
