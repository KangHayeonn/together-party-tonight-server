package webProject.togetherPartyTonight.domain.club.info.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webProject.togetherPartyTonight.domain.club.info.dto.NewClubRequest;

import javax.persistence.*;
import java.awt.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "master_id", nullable = false)
    //FK
    private Long masterId;

    @Column(nullable = false)
    private String name;

    @Column(name = "club_category" , nullable = false)
    private String clubCategory;

    @Column(nullable = false)
    private Integer minimum;

    @Column(nullable = false)
    private Integer maximum;

    @Column(name = "club_details", nullable = false)
    private String clubDetails;

    @Column(name = "club_tags",columnDefinition = "json", nullable = false)
    private String clubTags;

    @Column(name = "club_point", columnDefinition = "point", nullable = false)
    private Point clubPoint;

    @Column(name = "club_state", nullable = false)
    private String clubState;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column (name="modified_date", nullable = false)
    private LocalDateTime modifiedDate;

    public Club (NewClubRequest clubRequest) {
        //this.masterId =
        this.name= clubRequest.getName();
        this.clubState=clubRequest.getClubState();
        //this.clubPoint=
        this.clubCategory=clubRequest.getClubCategory();
        this.clubDetails= clubRequest.getClubDetails();
        //this.clubTags = clubRequest.getClubTags();
        this.maximum = clubRequest.getMaximum();
        this.minimum= clubRequest.getMinimum();
        this.createdDate= LocalDateTime.now();
        this.modifiedDate= LocalDateTime.now();
    }


}



