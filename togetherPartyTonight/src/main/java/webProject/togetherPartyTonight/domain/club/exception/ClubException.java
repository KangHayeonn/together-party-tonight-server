package webProject.togetherPartyTonight.domain.club.exception;

import lombok.Getter;
import webProject.togetherPartyTonight.global.error.CommonException;
import webProject.togetherPartyTonight.global.error.ErrorCode;
import webProject.togetherPartyTonight.global.error.ErrorInterface;

@Getter
public class ClubException extends CommonException {

    public ClubException(ErrorInterface errorCode) {
        super(errorCode);
    }
}
