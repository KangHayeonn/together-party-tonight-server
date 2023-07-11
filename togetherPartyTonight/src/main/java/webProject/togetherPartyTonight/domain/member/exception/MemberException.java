package webProject.togetherPartyTonight.domain.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webProject.togetherPartyTonight.global.error.CommonException;
import webProject.togetherPartyTonight.global.error.ErrorInterface;

@Getter
public class MemberException extends CommonException {

    private ErrorInterface errorInterface;

    public MemberException(ErrorInterface errorCode) {
        super(errorCode);
    }
}
