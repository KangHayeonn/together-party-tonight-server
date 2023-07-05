package webProject.togetherPartyTonight.domain.club.dto.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("가입 신청 취소")
public class DeleteMyAppliedRequestDto {
    @NotNull (message = "memberId는 필수 입력 값입니다.")
    @ApiModelProperty(value = "신청자 id", example = "1")
    private Long memberId;

    @NotNull (message = "clubSignupId는 필수 입력 값입니다.")
    @ApiModelProperty(value = "가입신청 id", example = "1")
    private Long clubSignupId;
}
