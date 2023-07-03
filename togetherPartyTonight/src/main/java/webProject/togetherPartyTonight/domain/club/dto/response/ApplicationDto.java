package webProject.togetherPartyTonight.domain.club.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDto {
    private Long clubSignupId;
    private Long clubId;
    private String clubName;
    private Long userId;
    private String nickName;
    private LocalDateTime signupDate;
    private String approvalStatus;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
