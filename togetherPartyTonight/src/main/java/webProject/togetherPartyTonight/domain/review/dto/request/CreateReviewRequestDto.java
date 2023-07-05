package webProject.togetherPartyTonight.domain.review.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import webProject.togetherPartyTonight.domain.club.entity.Club;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.domain.review.entity.Review;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class CreateReviewRequestDto {
    @NotNull(message = "clubId는 필수 입력 값입니다.")
    Long clubId;

    @NotNull(message = "memberId는 필수 입력 값입니다.")
    Long memberId;
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
