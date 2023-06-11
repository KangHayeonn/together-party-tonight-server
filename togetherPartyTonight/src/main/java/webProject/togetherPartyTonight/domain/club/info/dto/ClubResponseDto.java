package webProject.togetherPartyTonight.domain.club.info.dto;

import lombok.*;
import org.springframework.data.geo.Point;
import webProject.togetherPartyTonight.domain.club.info.entity.Club;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubResponseDto {
//    private String nickname;
    private Long userId;
    private String clubName;
    private String clubCategory;
    private Integer clubMaximum;
    private Integer clubMinimum;
    private String clubContent;
    private String clubTags;
    private String address;
    private Float latitude;
    private Float longitude;
    private LocalDate meetingDate;

    public ClubResponseDto toDto (Club club) {
//        this.nickname=club.getNickname();
        this.clubName= club.getClubName();
        this.clubCategory=club.getClubCategory();
        this.clubContent= club.getClubContent();
        this.clubMaximum = club.getClubMaximum();
        this.clubMinimum= club.getClubMinimum();
        this.userId = club.getMasterId();
//        this.latitude= (float) club.getClubPoint().getX();
//        this.longitude= (float) club.getClubPoint().getY();
        this.clubTags = club.getClubTags();
        this.address=club.getAddress();
        this.meetingDate= club.getMeetingDate();
        return this;
    }
}
