package webProject.togetherPartyTonight.domain.review.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import webProject.togetherPartyTonight.domain.club.entity.Club;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.domain.review.entity.Review;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class CreateReviewRequestDto {
    @NotNull(message = "clubId는 필수 입력 값입니다.")
    Long clubId;

    @Min(value = 0, message = "최소 0점이상 평점을 매겨야 합니다.")
    @Max(value = 5, message = "최대 5점까지 평점을 줄 수 있습니다.")
    @NotNull(message = "rating은 필수 입력 값입니다.")
    Integer rating;

    @Size(max=300, message = "reviewContent는 최대 300자를 넘을 수 없습니다.")
    @NotNull(message = "reviewContent는 필수 입력 값입니다.")
    String reviewContent;

    public Review toEntity(Club club, Member member, String image) {
        return Review.builder()
                .club(club)
                .member(member)
                .reviewContent(this.reviewContent)
                .rating(rating)
                .imageUrl(image)
                .build();
    }
}
