package webProject.togetherPartyTonight.domain.billing.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BillingState {

    결제대기 ("결제대기"),
    정산완료("정산완료");

    private String billingState;
}

