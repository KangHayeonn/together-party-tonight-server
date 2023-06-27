package webProject.togetherPartyTonight.domain.club.dto.response;

import lombok.Builder;
import lombok.Data;
import webProject.togetherPartyTonight.domain.club.entity.ClubCategory;

import java.util.List;

@Data
@Builder
public class MyOwnedClub {
    private String clubName;
    private ClubCategory clubCategory;
    private Integer clubMaximum;
    private Integer clubMinimum;
    private String clubContent;
    private List<String> clubTags;
    Float latitude;
    Float longitude;
    String address;

    String meetingDate;
}