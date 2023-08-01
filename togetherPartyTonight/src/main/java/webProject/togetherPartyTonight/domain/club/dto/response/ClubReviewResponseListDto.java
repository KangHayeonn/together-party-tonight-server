package webProject.togetherPartyTonight.domain.club.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import webProject.togetherPartyTonight.domain.review.dto.response.GetReviewDetailResponseDto;
import webProject.togetherPartyTonight.domain.search.dto.SearchResponseDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClubReviewResponseListDto {
    @ApiModelProperty(value = "리뷰 리스트")
    List<GetReviewDetailResponseDto> reviewList;

    @ApiModelProperty(value = "현재 페이지의 응답 갯수", example = "20")
    Integer count;

    @ApiModelProperty(value = "검색된 총 응답 갯수", example = "95")
    Long totalCount;

    @ApiModelProperty(value = "모든 리뷰의 평균 평점", example = "3.75")
    Float avgRating;

}
