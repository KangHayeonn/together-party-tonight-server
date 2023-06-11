package webProject.togetherPartyTonight.domain.member.exception;

import lombok.Getter;
import webProject.togetherPartyTonight.global.error.ErrorCode;

@Getter
public class MemberException extends RuntimeException{
    private ErrorCode errorCode;

    public MemberException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode= errorCode;
    }
}
