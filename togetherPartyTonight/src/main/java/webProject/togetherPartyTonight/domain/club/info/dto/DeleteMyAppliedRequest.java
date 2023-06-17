package webProject.togetherPartyTonight.domain.club.info.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteMyAppliedRequest {
    @NotNull
    private Long userId;

    @NotNull
    private Long clubSignupId;
}
