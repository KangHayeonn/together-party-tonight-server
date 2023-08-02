package webProject.togetherPartyTonight.domain.billing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
@ApiModel("모임 정산 내역 요청")
public class ClubBillingHistoryRequestDto {
    @ApiModelProperty(notes = "모임 아이디", dataType = "Long")
    private Long clubId;
}
