package webProject.togetherPartyTonight.domain.club.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import webProject.togetherPartyTonight.domain.club.entity.ApprovalState;
import webProject.togetherPartyTonight.domain.club.entity.Club;
import webProject.togetherPartyTonight.domain.club.entity.ClubSignup;
import webProject.togetherPartyTonight.domain.member.entity.Member;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteClubAndSignupRequestDto {

    @NotNull (message = "memberId는 필수 입력 값입니다.")
    private Long memberId ;

    @NotNull (message = "clubId는 필수 입력 값입니다.")
    private Long clubId ;

    public ClubSignup toClubSignup (Club club, Member member, Member master) {
        return ClubSignup.builder()
                .club(club)
                .clubMember(member)
                .clubMaster(master)
                .clubSignupApprovalState(ApprovalState.PENDING)
                .clubSignupDate(LocalDateTime.now())
                .clubSignupApprovalDate(null)
                .build();
    }
}
