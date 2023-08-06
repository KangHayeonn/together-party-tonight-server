package webProject.togetherPartyTonight.domain.billing.exception;

import lombok.Getter;
import webProject.togetherPartyTonight.global.error.CommonException;
import webProject.togetherPartyTonight.global.error.ErrorInterface;

@Getter
public class BillingException extends CommonException {
    private ErrorInterface errorCode;

    public BillingException(ErrorInterface errorCode) {
        super(errorCode);
        this.errorCode= errorCode;
    }
}
