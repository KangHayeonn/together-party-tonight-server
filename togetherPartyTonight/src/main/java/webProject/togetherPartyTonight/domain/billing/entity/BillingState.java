package webProject.togetherPartyTonight.domain.billing.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BillingState {

    WAIT("결제대기"),
    COMPLETED("정산완료");

    private String billingState;

    public String getStateString() {
        return billingState;
    }
}

