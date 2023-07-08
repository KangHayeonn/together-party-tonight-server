package webProject.togetherPartyTonight.domain.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.asm.Advice;
import webProject.togetherPartyTonight.domain.review.entity.Review;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyPageReviewResponseDto {
    private Long reviewId;
    private Integer rating;
    private String reviewContent;
    private String nickName;

    private String clubName;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public MyPageReviewResponseDto toDto (Review r) {
        return MyPageReviewResponseDto.builder()
                .reviewId(r.getReviewId())
                .rating(r.getRating())
                .reviewContent(r.getReviewContent())
                .nickName(r.getMember().getNickname())
                .clubName(r.getClub().getClubName())
                .createdDate(r.getCreatedDate())
                .modifiedDate(r.getModifiedDate())
                .build();
    }
}
