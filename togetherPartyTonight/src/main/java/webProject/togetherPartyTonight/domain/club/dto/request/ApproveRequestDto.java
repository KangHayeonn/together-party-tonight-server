package webProject.togetherPartyTonight.domain.club.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("모임 가입신청에 대한 응답(승인/거절)")
public class ApproveRequestDto {

    @NotNull (message = "clubSignupId는 필수 입력 값입니다.")
    @ApiModelProperty(value = "가입신청 id", example = "1")
    private Long clubSignupId;

    @NotNull (message = "approve는 필수 입력 값입니다.")
    @ApiModelProperty(value = "응답 결과(승인/거절)",example = "true", allowableValues = "true,false")
    private Boolean approve;


}
