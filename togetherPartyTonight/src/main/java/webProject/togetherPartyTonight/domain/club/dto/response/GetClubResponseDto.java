package webProject.togetherPartyTonight.domain.club.dto.response;

import lombok.*;
import webProject.togetherPartyTonight.domain.club.entity.Club;
import webProject.togetherPartyTonight.domain.club.entity.ClubCategory;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private String clubContent;
    private String clubTags;
    private String address;
    private Float latitude;
    private Float longitude;
    private String meetingDate;
    private String image;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public GetClubResponseDto toDto (Club club) {
        this.nickName=club.getMaster().getNickname();
        this.clubName= club.getClubName();
        this.clubCategory=club.getClubCategory();
        this.clubContent= club.getClubContent();
        this.clubMaximum = club.getClubMaximum();
        this.userId = club.getMaster().getId();
        this.latitude= (float) club.getClubPoint().getX();
        this.longitude= (float) club.getClubPoint().getY();
        this.clubTags = club.getClubTags();
        this.address=club.getAddress();
        this.meetingDate= String.valueOf(club.getMeetingDate());
        this.image=club.getImage();
        this.createdDate=club.getCreatedDate();
        this.modifiedDate=club.getModifiedDate();
        return this;
    }
}
