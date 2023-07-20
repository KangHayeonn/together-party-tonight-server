package webProject.togetherPartyTonight.domain.billing.dto;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@ApiModel("나의 정산 내역 요청 응답")
public class MyBillingHistoryResponseDto {
    private List<BillingHistoryDto> myBillingHistoryDtoList;
}

