package webProject.togetherPartyTonight.domain.billing.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import webProject.togetherPartyTonight.domain.billing.entity.Billing;
import webProject.togetherPartyTonight.domain.billing.entity.BillingHistory;
import webProject.togetherPartyTonight.domain.billing.entity.BillingState;
import webProject.togetherPartyTonight.domain.club.entity.ClubMember;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Getter
@Builder
@AllArgsConstructor
@ApiModel("정산하기 요청 응답")
public class BillingPaymentResponseDto {

    @ApiModelProperty(notes = "정산내역 아이디", value = "Long", example = "11")
    private Long billingHistoryId;

    @ApiModelProperty(notes = "정산 아이디", value = "Long", example = "10")
    private Long billingId;

    @ApiModelProperty(notes = "정산 가격", value = "Integer", example = "10000")
    private Integer price;

    @ApiModelProperty(notes = "정산 상태", value = "String", example = "결제대기, 정산완료")
    private String billingState;

    public static BillingPaymentResponseDto toDto(BillingHistory billingHistory) {
        return BillingPaymentResponseDto.builder()
                .billingHistoryId(billingHistory.getId())
                .billingId(billingHistory.getBilling().getId()).price(billingHistory.getPrice())
                .billingState(billingHistory.getBillingState().getStateString())
                .build();
    }
}