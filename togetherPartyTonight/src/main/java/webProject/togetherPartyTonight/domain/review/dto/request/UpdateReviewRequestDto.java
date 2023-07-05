package webProject.togetherPartyTonight.domain.review.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webProject.togetherPartyTonight.domain.review.entity.Review;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateReviewRequestDto {
    @NotNull(message = "reviewId는 필수 입력 값입니다.")
    Long reviewId;

    @NotNull(message = "memberId는 필수 입력 값입니다.")
    Long memberId;

    @NotNull(message = "rating은 필수 입력 값입니다.")
    Integer rating;

    @Size(max=300, message = "reviewContent는 최대 300자를 넘을 수 없습니다.")
    @NotNull(message = "reviewContent는 필수 입력 값입니다.")
    String reviewContent;

    public void modify(Review review, String imageUrl) {
        review.setRating(rating);
        review.setReviewContent(reviewContent);
        review.setImageUrl(imageUrl);
    }
}
