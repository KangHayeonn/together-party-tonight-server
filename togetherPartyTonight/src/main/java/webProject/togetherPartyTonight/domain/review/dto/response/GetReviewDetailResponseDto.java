package webProject.togetherPartyTonight.domain.review.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;
import webProject.togetherPartyTonight.domain.club.entity.Club;
import webProject.togetherPartyTonight.domain.review.entity.Review;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value = "리뷰 상세 조회")
public class GetReviewDetailResponseDto {
    @ApiModelProperty(name = "작성자 id")
    Long memberId;
    @ApiModelProperty(name = "작성자 닉네임")
    String nickname;
    @ApiModelProperty(name = "리뷰 내용")
    String reviewContent;
    @ApiModelProperty(name = "별점")
    Integer rating;
    @ApiModelProperty(name = "리뷰 대상 모임의 제목")
    String clubName;
    @ApiModelProperty(name = "모임 주소")
    String address;
    @ApiModelProperty(name = "모임 날짜")
    LocalDateTime meetingDate;
    @ApiModelProperty(name = "리뷰 최초 작성 시각")
    LocalDateTime createdDate;
    @ApiModelProperty(name = "리뷰 수정 시각")
    LocalDateTime modifiedDate;
    @ApiModelProperty(name = "리뷰 이미지")
    String image;

    public GetReviewDetailResponseDto toDto(Review review) {
        Club club = review.getClub();
        return GetReviewDetailResponseDto.builder()
                .memberId(review.getMember().getId())
                .nickname(review.getMember().getNickname())
                .reviewContent(review.getReviewContent())
                .rating(review.getRating())
                .clubName(club.getClubName())
                .address(club.getAddress())
                .meetingDate(club.getMeetingDate())
                .createdDate(club.getCreatedDate())
                .modifiedDate(review.getModifiedDate())
                .image(review.getImageUrl())
                .build();
    }
}
