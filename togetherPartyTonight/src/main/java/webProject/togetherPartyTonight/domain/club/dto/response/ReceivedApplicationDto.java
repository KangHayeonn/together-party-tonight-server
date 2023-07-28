package webProject.togetherPartyTonight.domain.club.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("내가 만든 모임에 신청한 사람들 내역")
public class ReceivedApplicationDto {
    @ApiModelProperty(value = "모임 신청 id", example = "1")
    private Long clubSignupId;
    @ApiModelProperty(value = "모임 id", example = "1")
    private Long clubId;
    @ApiModelProperty(value = "모임 제목", example = "오늘 테니스 치실 분 구합니다")
    private String clubName;
    @ApiModelProperty(value = "모임장 id", example = "1")
    private Long memberId;
    @ApiModelProperty(value = "모임 신청한 사람 닉네임", example = "어피치")
    private String nickName;
    @ApiModelProperty(value = "모임 신청 일자", example = "2023-07-05T17:16")
    private LocalDateTime signupDate;
    @ApiModelProperty(value = "승인 여부", example = "PENDING", allowableValues = "PENDING, APPROVE, REFUSE, KICKOUT")
    private String approvalStatus;

    @ApiModelProperty(value = "모임장 프로필 이미지")
    private String profileImage;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
