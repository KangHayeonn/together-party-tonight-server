package webProject.togetherPartyTonight.domain.review.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateReviewResponseDto {
    @ApiModelProperty(name = "리뷰 id")
    private Long reviewId;
}
