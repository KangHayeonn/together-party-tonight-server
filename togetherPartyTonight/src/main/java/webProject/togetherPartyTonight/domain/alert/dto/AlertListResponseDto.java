package webProject.togetherPartyTonight.domain.alert.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("알림 목록 응답")
public class AlertListResponseDto {

    @ApiModelProperty(notes = "알림 리스트", dataType = "List")
    List<AlertDto> alertList;
}