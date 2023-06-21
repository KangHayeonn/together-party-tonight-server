package webProject.togetherPartyTonight.domain.club.info.dto;

import lombok.*;
import webProject.togetherPartyTonight.domain.club.info.entity.Club;
import webProject.togetherPartyTonight.domain.club.info.entity.ClubCategory;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubDetailResponse {
    private String nickName;
    private Long userId;
    private String clubName;
    private ClubCategory clubCategory;
    private Integer clubMaximum;
    private Integer clubMinimum;
    private String clubContent;
    private String clubTags;
    private String address;
    private Float latitude;
    private Float longitude;
    private LocalDate meetingDate;

    public ClubDetailResponse toDto (Club club) {
//        this.nickname=club.getNickname();
        this.clubName= club.getClubName();
        this.clubCategory=club.getClubCategory();
        this.clubContent= club.getClubContent();
        this.clubMaximum = club.getClubMaximum();
        this.clubMinimum= club.getClubMinimum();
        this.userId = club.getMaster().getId();
        this.latitude= (float) club.getClubPoint().getX();
        this.longitude= (float) club.getClubPoint().getY();
        this.clubTags = club.getClubTags();
        this.address=club.getAddress();
        this.meetingDate= club.getMeetingDate();
        return this;
    }
}
