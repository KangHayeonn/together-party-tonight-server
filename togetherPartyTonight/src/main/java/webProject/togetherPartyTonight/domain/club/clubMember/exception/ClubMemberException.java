package webProject.togetherPartyTonight.domain.club.clubMember.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webProject.togetherPartyTonight.global.error.ErrorCode;

@Getter
@AllArgsConstructor
public class ClubMemberException extends RuntimeException{
    private ErrorCode errorCode;
}
