package webProject.togetherPartyTonight.domain.club.exception;

import lombok.Getter;
import webProject.togetherPartyTonight.global.error.ErrorCode;

@Getter
public class ClubException extends RuntimeException{
    private ErrorCode errorCode;

    public ClubException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode= errorCode;
    }
}
