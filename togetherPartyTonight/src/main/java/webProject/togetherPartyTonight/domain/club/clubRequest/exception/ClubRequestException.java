package webProject.togetherPartyTonight.domain.club.clubRequest.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webProject.togetherPartyTonight.global.error.ErrorCode;

@Getter
@AllArgsConstructor
public class ClubRequestException extends RuntimeException{
    private ErrorCode errorCode;
}
