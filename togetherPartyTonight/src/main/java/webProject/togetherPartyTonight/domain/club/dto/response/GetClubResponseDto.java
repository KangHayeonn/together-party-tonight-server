package webProject.togetherPartyTonight.domain.club.dto.response;

import lombok.*;
import webProject.togetherPartyTonight.domain.club.entity.Club;
import webProject.togetherPartyTonight.domain.club.entity.ClubCategory;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetClubResponseDto {
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
    private String image;

    public GetClubResponseDto toDto (Club club) {
        this.nickName=club.getMaster().getNickname();
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
        this.image=club.getImage();
        return this;
    }
}
