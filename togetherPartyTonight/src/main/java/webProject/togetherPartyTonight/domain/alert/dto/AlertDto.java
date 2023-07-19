package webProject.togetherPartyTonight.domain.alert.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import webProject.togetherPartyTonight.domain.alert.entity.Alert;

@Getter
@Builder
public class AlertDto {
    @ApiModelProperty(notes = "알림 아이디", dataType = "Long")
    private Long alertId;

    @ApiModelProperty(notes = "알림 타입", dataType = "String")
    private String alertType;


    @ApiModelProperty(notes = "알림 내용", dataType = "String")
    private String alertContent;

    @ApiModelProperty(notes = "알림 확인여부", dataType = "Boolean")
    private Boolean checkStatus;

    @ApiModelProperty(notes = "알림 생성 시간", dataType = "String")
    private String alertCreateDateTime;

    @ApiModelProperty(notes = "읽은 시간", dataType = "String")
    private String alertReadDateTime;

    public static AlertDto toDto(Alert alert) {
        return AlertDto.builder()
                .alertId(alert.getId())
                .alertType(alert.getAlertType().toString())
                .alertContent(alert.getAlert_content())
                .checkStatus(alert.isAlert_check_state())
                .alertCreateDateTime(alert.getCreatedDate().toString())
                .alertReadDateTime(alert.getModifiedDate().toString())
                .build();
    }
}
