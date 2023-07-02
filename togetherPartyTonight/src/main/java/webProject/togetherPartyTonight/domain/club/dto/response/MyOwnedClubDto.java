package webProject.togetherPartyTonight.domain.club.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import webProject.togetherPartyTonight.domain.club.entity.ClubCategory;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class MyOwnedClubDto {
    private String clubName;
    private ClubCategory clubCategory;
    private Integer clubMaximum;
    private String clubContent;
    private List<String> clubTags;
    private Float latitude;
    private Float longitude;
    private String address;

    private LocalDateTime meetingDate;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
