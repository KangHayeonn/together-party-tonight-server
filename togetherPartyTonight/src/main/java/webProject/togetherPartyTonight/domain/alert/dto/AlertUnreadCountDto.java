package webProject.togetherPartyTonight.domain.alert.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlertUnreadCountDto {
    @ApiModelProperty(notes = "안 읽은 알림 수", dataType = "Integer")
    private Integer alertUnreadCount;
}
