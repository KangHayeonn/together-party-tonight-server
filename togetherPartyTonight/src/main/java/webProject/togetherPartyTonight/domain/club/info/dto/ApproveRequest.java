package webProject.togetherPartyTonight.domain.club.info.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApproveRequest {

    @NotNull
    private Long clubSignupId;

    @NotNull
    private Boolean approve;


}
