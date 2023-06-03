package webProject.togetherPartyTonight.domain.club.info.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webProject.togetherPartyTonight.global.error.ErrorCode;

@Getter
@AllArgsConstructor
public class ClubException extends RuntimeException{
    private ErrorCode errorCode;

}
