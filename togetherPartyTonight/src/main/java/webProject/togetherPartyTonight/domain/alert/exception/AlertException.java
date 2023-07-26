package webProject.togetherPartyTonight.domain.alert.exception;

import lombok.Getter;
import webProject.togetherPartyTonight.global.error.CommonException;
import webProject.togetherPartyTonight.global.error.ErrorInterface;

@Getter
public class AlertException extends CommonException {

    public AlertException(ErrorInterface errorInterface) {
        super(errorInterface);
    }
}
