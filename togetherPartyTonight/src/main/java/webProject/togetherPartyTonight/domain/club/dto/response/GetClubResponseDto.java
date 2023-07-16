package webProject.togetherPartyTonight.domain.club.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import webProject.togetherPartyTonight.domain.club.entity.Club;
import webProject.togetherPartyTonight.domain.club.entity.ClubCategory;

import javax.swing.text.html.parser.TagElement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("모임 상세 조회")
public class GetClubResponseDto {
    @ApiModelProperty(value = "모임장 닉네임", example = "어피치")
    private String nickName;
    @ApiModelProperty(value = "모임장 id", example = "1")
    private Long memberId;
    @ApiModelProperty(value = "모임 제목", example = "오늘 테니스 치실 분 구합니다")
    private String clubName;
    @ApiModelProperty(value = "모임 카테고리", example = "운동")
    private ClubCategory clubCategory;
    @ApiModelProperty(value = "모임 최대 인원", example = "10")
    private Integer clubMaximum;
    @ApiModelProperty(value = "모임 글 내용", example = "테니스 같이 치실 초보 분 구합니다. 저도 초보에요.")
    private String clubContent;
    @ApiModelProperty(value = "모임 태그", example = "[\"테니스\",\"다이어트\",\"오운완\"]")
    private List<String> clubTags;
    @ApiModelProperty(value = "모임 주소", example = "서울시 서대문구 창천동")
    private String address;
    @ApiModelProperty(value = "모임 주소의 위도", example = "37.55920")
    private Float latitude;
    @ApiModelProperty(value = "모임 주소의 경도", example = "126.942310")
    private Float longitude;
    @ApiModelProperty(value = "모임 날짜와 시간", example = "2023-07-05:12:30")
    private LocalDateTime meetingDate;
    @ApiModelProperty(value = "첨부 이미지", example = "https://our-bucket-example.s3.ap-northeast-2.amazonaws.com/맛집_default.jpg")
    private String image;
    @ApiModelProperty(value = "최초 작성 시각", example = "2023-07-05:12:30")
    private LocalDateTime createdDate;
    @ApiModelProperty(value = "최근 수정 시각", example = "2023-07-05:12:30")
    private LocalDateTime modifiedDate;
    @ApiModelProperty(value = "모임 모집 상태", example = "true", allowableValues = "true,false", notes = "true : 모집중, false : 모집 완료")
    private Boolean isRecruit;
    @ApiModelProperty(value = "현재 모임 인원", example = "3")
    private Integer memberCount;
    @ApiModelProperty(value = "모임장 별점 평균", example = "4.5")
    private Float ratingAvg;
    @ApiModelProperty(value = "모임장 리뷰 갯수", example = "13")
    private Integer reviewCnt;

    public GetClubResponseDto toDto (Club club, List<String> tags) {
        this.nickName=club.getMaster().getNickname();
        this.clubName= club.getClubName();
        this.clubCategory=club.getClubCategory();
        this.clubContent= club.getClubContent();
        this.clubMaximum = club.getClubMaximum();
        this.memberId = club.getMaster().getId();
        this.latitude= (float) club.getClubPoint().getX();
        this.longitude= (float) club.getClubPoint().getY();
        this.clubTags = tags;
        this.address=club.getAddress();
        this.meetingDate= club.getMeetingDate();
        this.image=club.getImage();
        this.createdDate=club.getCreatedDate();
        this.modifiedDate=club.getModifiedDate();
        this.isRecruit=club.getClubState();
        this.memberCount=club.getClubMembers().size();
        this.ratingAvg= club.getMaster().getRatingAvg();
        this.reviewCnt= club.getMaster().getReviewCount();
        return this;
    }
}
