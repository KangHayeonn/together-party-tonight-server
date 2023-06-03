package webProject.togetherPartyTonight.domain.club.info.dto;

import lombok.Builder;
import lombok.Data;
import webProject.togetherPartyTonight.domain.club.info.entity.Club;

import java.time.LocalDateTime;

@Data
public class ClubResponseDto {
    private Long id;
    private Long masterId;
    private String name;
    private String clubCategory;
    private Integer maximum;
    private Integer minimum;
    private String clubDetails;
//    private String clubTags; //json
//    private Point clubPoint;
    private String clubState;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public ClubResponseDto (Club club) {
        this.id=club.getId();
        this.masterId = club.getMasterId();
        this.name= club.getName();
        this.clubState=club.getClubState();
//        this.clubPoint= club.getClubPoint();
//        this.clubTags = club.getClubTags();
        this.clubCategory=club.getClubCategory();
        this.clubDetails= club.getClubDetails();
        this.maximum = club.getMaximum();
        this.minimum= club.getMinimum();
        this.createdDate= LocalDateTime.now();
        this.modifiedDate= LocalDateTime.now();
    }
}
