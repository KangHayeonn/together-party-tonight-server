package webProject.togetherPartyTonight.domain.billing.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BillingState {

    wait ("결제대기"),
    completed ("정산완료");

    private String billingState;
}

