package webProject.togetherPartyTonight.domain.billing.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import webProject.togetherPartyTonight.domain.billing.entity.BillingHistory;

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

    public static BillingHistoryDto toDto(BillingHistory billingHistory) {
        return BillingHistoryDto.builder()
                .id(billingHistory.getId())
                .memberId(billingHistory.getClubMember().getMember().getId())
                .nickname(billingHistory.getClubMember().getMember().getNickname())
                .price(billingHistory.getPrice())
                .billingState(billingHistory.getBillingState().getStateString())
                .build();
    }
}
