package webProject.togetherPartyTonight.domain.search.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.security.core.parameters.P;
import webProject.togetherPartyTonight.domain.club.entity.Club;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value = "개별 모임")
public class SearchResponseDto {
    @ApiModelProperty(name = "모임 id", example = "1")
    private Long clubId;
    @ApiModelProperty(name = "모임 제목", example = "테니스 모임 구합니다.")
    private String clubName;
    @ApiModelProperty(name = "모임 카테고리", example = "운동")
    private String clubCategory;
    @ApiModelProperty(name = "모임 태그", example = "[테니스,다이어트]")
    private List<String> clubTags;
    @ApiModelProperty(name = "모임 본문", example = "같이 테니스 연습하실 초보 분 구합니다.")
    private String clubContent;
    @ApiModelProperty(name = "모임 최대 인원", example = "5")
    private Integer clubMaximum;
    @ApiModelProperty(name = "위도", example = "37.55920")
    private Float latitude;
    @ApiModelProperty(name = "경도", example = "126.942310")
    private Float longitude;
    @ApiModelProperty(name = "모임 주소", example = "서울시 서대문구 창천동")
    private String address;
    @ApiModelProperty(name = "모임 날짜와 시간", example = "2023-07-03T15:30")
    private LocalDateTime meetingDate;
    @ApiModelProperty(name = "모집 글 속 사진", example = "https://www.google.com/")
    private String image;
    @ApiModelProperty(name = "모집 상태", example = "true")
    private Boolean isRecruit;
    @ApiModelProperty(name = "현재 모집된 인원", example = "3")
    private Integer memberCount;
    @ApiModelProperty(name = "모집장 id", example = "3")
    private Long memberId;
    @ApiModelProperty(name = "모집장 닉네임", example = "apeach")
    private String nickName;
    @ApiModelProperty(name = "모집장 평점", example = "4.5")
    private Float ratingAvg;
    @ApiModelProperty(name = "모집장 리뷰 갯수", example = "7")
    private Integer reviewCnt;
    @ApiModelProperty(name = "모임 글 생성 일자", example = "2023-07-03T15:30:41.568606")
    private LocalDateTime createdDate;
    @ApiModelProperty(name = "모임 글 수정 일자", example = "2023-07-03T15:30:41.568606")
    private LocalDateTime modifiedDate;

    public SearchResponseDto toDto (Club c, List<String> tags, Point point) {
        return SearchResponseDto.builder()
                .clubId(c.getClubId())
                .clubName(c.getClubName())
                .clubCategory(String.valueOf(c.getClubCategory()))
                .clubTags(tags)
                .clubContent(c.getClubContent())
                .clubMaximum(c.getClubMaximum())
                .longitude((float)point.getX())
                .latitude((float)point.getY())
                .isRecruit(c.getClubState())
                .meetingDate(c.getMeetingDate())
                .image(c.getImage())
                .address(c.getAddress())
                .memberCount(c.getClubMembers().size())
                .memberId(c.getMaster().getId())
                .nickName(c.getMaster().getNickname())
                .ratingAvg(c.getMaster().getRatingAvg())
                .reviewCnt(c.getMaster().getReviewCount())
                .createdDate(c.getCreatedDate())
                .modifiedDate(c.getModifiedDate())
                .build();
    }
}
