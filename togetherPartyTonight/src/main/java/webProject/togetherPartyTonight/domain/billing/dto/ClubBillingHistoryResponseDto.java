package webProject.togetherPartyTonight.domain.billing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@ApiModel("모임 정산 내역 요청 응답")
public class ClubBillingHistoryResponseDto {
    @ApiModelProperty(notes = "모임 정산 내역 리스트",value = "List")
    private List<BillingHistoryDto> clubBillingHistoryDtoList;
}
