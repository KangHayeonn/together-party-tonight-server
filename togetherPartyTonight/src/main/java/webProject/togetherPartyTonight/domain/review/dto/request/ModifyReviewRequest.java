package webProject.togetherPartyTonight.domain.review.dto.request;

import lombok.Builder;
import lombok.Data;
import webProject.togetherPartyTonight.domain.review.entity.Review;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class ModifyReviewRequest {
    @NotNull(message = "reviewId는 필수 입력 값입니다.")
    Long reviewId;

    @NotNull(message = "userId는 필수 입력 값입니다.")
    Long userId;

    @NotNull(message = "rating은 필수 입력 값입니다.")
    Integer rating;

    @Size(max=300, message = "reviewContent는 최대 300자를 넘을 수 없습니다.")
    @NotNull(message = "reviewContent는 필수 입력 값입니다.")
    String reviewContent;

    public void modify(Review review) {
        review.setRating(rating);
        review.setReviewContent(reviewContent);
    }
}
