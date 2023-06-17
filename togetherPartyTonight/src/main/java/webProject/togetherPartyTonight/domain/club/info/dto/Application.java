package webProject.togetherPartyTonight.domain.club.info.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Application {
    private Long clubSignupId;
    private Long clubId;
    private String clubName;
    private Long userId;
    private String userName;
    private String signupDate;
    private String approvalStatus;
}
