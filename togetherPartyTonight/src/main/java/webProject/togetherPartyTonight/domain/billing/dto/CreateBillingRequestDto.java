package webProject.togetherPartyTonight.domain.billing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
@ApiModel("정산 요청")
public class CreateBillingRequestDto {
    @ApiModelProperty(notes = "모임 아이디", dataType = "Long")
    private Long clubId;

    @ApiModelProperty(notes = "정산 가격", dataType = "Long")
    private Integer price;
}
