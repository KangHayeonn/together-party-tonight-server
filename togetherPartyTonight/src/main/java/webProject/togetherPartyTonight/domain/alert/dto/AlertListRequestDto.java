package webProject.togetherPartyTonight.domain.alert.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
@ApiModel("알림 리스트 요청")
public class AlertListRequestDto {

    @ApiModelProperty(notes = "마지막 seq", dataType = "Long")
    private Long lastSeq;

    @ApiModelProperty(notes = "요청 개수", dataType = "Integer")
    private Integer listCount;

    @ApiModelProperty(notes = "모두 또는 안읽은 메시지 여부", dataType = "Boolean")
    private Boolean isAllOrNotRead;
}