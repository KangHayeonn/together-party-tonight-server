package webProject.togetherPartyTonight.domain.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewListDto {
    List<GetReviewDetailResponseDto> reviewList;
}
