package webProject.togetherPartyTonight.domain.club.info.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MyOwnedClub {
    private String clubName;
    private String clubCategory;
    private Integer clubMaximum;
    private Integer clubMinimum;
    private String clubContent;
    private List<String> clubTags;
    Float latitude;
    Float longitude;
    String address;

    String meetingDate;
}
