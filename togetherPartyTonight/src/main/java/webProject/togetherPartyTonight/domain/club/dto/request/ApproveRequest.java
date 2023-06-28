package webProject.togetherPartyTonight.domain.club.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApproveRequest {

    @NotNull (message = "clubSignupId는 필수 입력 값입니다.")
    private Long clubSignupId;

    @NotNull (message = "approve는 필수 입력 값입니다.")
    private Boolean approve;


}
