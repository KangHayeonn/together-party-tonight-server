package webProject.togetherPartyTonight.domain.billing.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@ApiModel("정산하기 요청 응답")
public class BillingPaymentRequestDto {
    @ApiModelProperty(notes = "정산내역 아이디", value = "Long",example = "12")
    private Long billingHistoryId;
}
