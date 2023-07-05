package webProject.togetherPartyTonight.domain.club.dto.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "모임 글 삭제, 가입 신청")
public class DeleteClubAndSignupRequestDto {

    @NotNull (message = "memberId는 필수 입력 값입니다.")
    @ApiModelProperty(value = "작성자 id", example = "1")
    private Long memberId ;

    @NotNull (message = "clubId는 필수 입력 값입니다.")
    @ApiModelProperty(value = "모임 id", example = "1")
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
