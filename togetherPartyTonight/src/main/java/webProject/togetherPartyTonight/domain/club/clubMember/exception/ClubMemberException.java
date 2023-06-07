package webProject.togetherPartyTonight.domain.club.clubMember.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webProject.togetherPartyTonight.global.error.ErrorCode;

@Getter
public class ClubMemberException extends RuntimeException{
    private ErrorCode errorCode;

    public ClubMemberException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode= errorCode;
    }
}
