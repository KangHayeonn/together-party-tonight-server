package webProject.togetherPartyTonight.domain.billing.exception;

import lombok.Getter;

@Getter
public class BillingException extends RuntimeException{
    private BillingErrorCode errorCode;

    public BillingException(BillingErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode= errorCode;
    }
}
