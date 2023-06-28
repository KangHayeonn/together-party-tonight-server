package webProject.togetherPartyTonight.domain.club.dto.response;

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
    private String nickName;
    private String signupDate;
    private String approvalStatus;
}
