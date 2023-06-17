package webProject.togetherPartyTonight.domain.club.info.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import webProject.togetherPartyTonight.domain.club.info.entity.ApprovalState;
import webProject.togetherPartyTonight.domain.club.info.entity.Club;
import webProject.togetherPartyTonight.domain.club.info.entity.ClubSignup;
import webProject.togetherPartyTonight.domain.member.entity.Member;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteAndSignupRequestDto {

    @NotNull
    private Long userId ;

    @NotNull
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
