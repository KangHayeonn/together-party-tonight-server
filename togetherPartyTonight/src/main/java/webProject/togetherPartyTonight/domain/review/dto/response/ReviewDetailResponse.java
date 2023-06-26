package webProject.togetherPartyTonight.domain.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webProject.togetherPartyTonight.domain.club.entity.Club;
import webProject.togetherPartyTonight.domain.review.entity.Review;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDetailResponse {
    Long userId; //작성자 아이디
    String nickname; //작성자 닉네임
    String reviewContent;
    Integer rating;
    String clubName;
    String address;
    LocalDate meetingDate;
    LocalDateTime createdDate;
    String image;

    public ReviewDetailResponse toDto(Review review) {
        Club club = review.getClub();
        return ReviewDetailResponse.builder()
                .userId(review.getMember().getId())
//                .nickname(review.getMember().getNickname())
                .reviewContent(review.getReviewContent())
                .rating(review.getRating())
                .clubName(club.getClubName())
                .address(club.getAddress())
                .meetingDate(club.getMeetingDate())
                .createdDate(club.getCreatedDate())
                .image(review.getImageUrl())
                .build();
    }
}
