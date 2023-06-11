package webProject.togetherPartyTonight.domain.club.clubRequest.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webProject.togetherPartyTonight.global.error.ErrorCode;

@Getter
public class ClubRequestException extends RuntimeException{
    private ErrorCode errorCode;

    public ClubRequestException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode= errorCode;
    }
}
