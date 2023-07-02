package webProject.togetherPartyTonight.domain.search.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SearchResponseDto {
    private Long clubId;
    private String clubName;
    private String clubCategory;
    private List<String> clubTags;
    private String clubContent;
    private Integer clubMaximum;
    private Float latitude;
    private Float longitude;
    private String address;
    private LocalDateTime meetingDate;
    private String image;
    private Boolean isRecruit;
    private Integer memberCount;
    private Long userId;
    private String nickName;
    private Float ratingAvg;
    private Integer reviewCnt;
    private Integer totalCnt;
    private Integer count;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}