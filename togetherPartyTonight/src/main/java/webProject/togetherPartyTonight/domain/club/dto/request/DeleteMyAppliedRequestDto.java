package webProject.togetherPartyTonight.domain.club.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteMyAppliedRequestDto {
    @NotNull (message = "memberId는 필수 입력 값입니다.")
    private Long memberId;

    @NotNull (message = "clubSignupId는 필수 입력 값입니다.")
    private Long clubSignupId;
}
