package webProject.togetherPartyTonight.domain.billing.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import webProject.togetherPartyTonight.domain.billing.entity.BillingHistory;
import webProject.togetherPartyTonight.domain.member.entity.Member;

@Builder
@Getter
public class BillingHistoryDto {
    @ApiModelProperty(notes = "정산내역 아이디", dataType = "Long")
    private Long id;
    @ApiModelProperty(notes = "멤버 아이디", dataType = "Long")
    private Long memberId;
    @ApiModelProperty(notes = "닉네임", dataType = "Long")
    private String nickname;
    @ApiModelProperty(notes = "금액", dataType = "Integer")
    private Integer price;
    @ApiModelProperty(notes = "정산 상태", dataType = "String")
    private String billingState;
    @ApiModelProperty(notes = "유저 프로필 이미지", dataType = "String")
    private String memberProfileImage;


    public static BillingHistoryDto toDto(BillingHistory billingHistory, Member member) {
        return BillingHistoryDto.builder()
                .id(billingHistory.getId())
                .memberId(billingHistory.getClubMember().getMember().getId())
                .nickname(billingHistory.getClubMember().getMember().getNickname())
                .price(billingHistory.getPrice())
                .billingState(billingHistory.getBillingState().getStateString())
                .memberProfileImage(member.getProfileImage())
                .build();
    }
}
