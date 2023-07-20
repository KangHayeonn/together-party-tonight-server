package webProject.togetherPartyTonight.domain.billing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@ApiModel("정산 요청 응답")
public class CreateBillingResponseDto {
    @ApiModelProperty(notes = "정산 아이디", dataType = "Long")
    private Long BillingId;
}