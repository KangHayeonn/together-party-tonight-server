package webProject.togetherPartyTonight.domain.club.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import webProject.togetherPartyTonight.domain.club.entity.ClubCategory;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@ApiModel("내가 만든 모임")
public class MyOwnedClubDto {
    @ApiModelProperty(value = "모임 제목", example = "오늘 테니스 치실 분 구합니다")
    private String clubName;
    @ApiModelProperty(value = "모임 카테고리", example = "운동")
    private ClubCategory clubCategory;
    @ApiModelProperty(value = "모임 최대 인원", example = "5")
    private Integer clubMaximum;
    @ApiModelProperty(value = "모임 글 내용", example = "테니스 같이 치실 초보 분 구합니다. 저도 초보에요.")
    private String clubContent;
    @ApiModelProperty(value = "모임 태그", example = "[\"테니스\",\"다이어트\",\"오운완\"]")
    private List<String> clubTags;
    @ApiModelProperty(value = "모임 주소의 위도", example = "37.55920")
    private Float latitude;
    @ApiModelProperty(value = "모임 주소의 경도", example = "126.942310")
    private Float longitude;
    @ApiModelProperty(value = "모임 주소", example = "서울시 서대문구 창천동")
    private String address;
    @ApiModelProperty(value = "모임 날짜와 시간", example = "2023-07-05:12:30")
    private LocalDateTime meetingDate;
    @ApiModelProperty(value = "첨부 이미지")
    private String image;

    @ApiModelProperty(value = "현재 모집된 인원", example = "3")
    private Integer appliedCount;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
