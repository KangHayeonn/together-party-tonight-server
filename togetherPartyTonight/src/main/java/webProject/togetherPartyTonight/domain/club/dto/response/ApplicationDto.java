package webProject.togetherPartyTonight.domain.club.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("내가 신청한 모임 내역")
public class ApplicationDto {
    @ApiModelProperty(value = "모임 신청 id", example = "1")
    private Long clubSignupId;
    @ApiModelProperty(value = "모임 id", example = "1")
    private Long clubId;
    @ApiModelProperty(value = "모임 제목", example = "오늘 테니스 치실 분 구합니다")
    private String clubName;
    @ApiModelProperty(value = "모임장 id", example = "1")
    private Long memberId;
    @ApiModelProperty(value = "모임장 닉네임", example = "어피치")
    private String nickName;
    @ApiModelProperty(value = "모임 신청 일자", example = "2023-07-05T17:16")
    private LocalDateTime signupDate;
    @ApiModelProperty(value = "승인 여부", example = "PENDING", allowableValues = "PENDING, APPROVE, REFUSE, KICKOUT")
    private String approvalStatus;
    @ApiModelProperty(value = "현재 모집된 인원", example = "3")
    private Integer appliedCount;

    @ApiModelProperty(value = "최대 모집 인원", example = "5")
    private Integer clubMaximum;

    @ApiModelProperty(value = "모임 태그", example = "[\"테니스\",\"다이어트\",\"오운완\"]")
    private List<String> clubTags;

    @ApiModelProperty(value = "모집 상태", example = "true")
    private Boolean clubState;

    @ApiModelProperty(value = "모집 날짜와 시간")
    private String meetingDate;

    @ApiModelProperty(value = "정산 완료 여부", example = "COMPLETED,WAIT,NO_REQUEST")
    private String billingState;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
