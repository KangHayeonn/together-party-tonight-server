package webProject.togetherPartyTonight.domain.club.info.entity;

import com.vividsolutions.jts.geom.Point;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import webProject.togetherPartyTonight.domain.club.info.dto.ClubRequestDto;
import webProject.togetherPartyTonight.global.common.BaseEntity;

import javax.persistence.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
public class Club extends BaseEntity {
    @Id
    @Column(name = "club_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clubId;

    @Column(name = "club_master_id", nullable = false)
//    //FK
    private Long masterId;

    @Column(name="club_name", nullable = false, length = 11)
    private String clubName;

    @Column(name = "club_category" , nullable = false, length = 255)
    private String clubCategory;

    @Column(name="club_minimum", nullable = false, length = 11)
    private Integer clubMinimum;

    @Column(name = "club_maximum",nullable = false, length = 11)
    private Integer clubMaximum;

    @Column(name = "club_content", nullable = false)
    private String clubContent;

    @Column(name = "club_tags", nullable = false)
    private String clubTags;

    @Column(name = "club_point", columnDefinition = "GEOMETRY")//,  nullable = false)
    private Point clubPoint;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "club_meeting_date")// nullable = false)
    private LocalDate meetingDate;

    @Column(name = "club_state", nullable = false, length = 1, columnDefinition = "tinyint")
    private Boolean clubState; //true: 모집중, false: 모집 완료


    public Club (ClubRequestDto clubRequest) {
        this.clubName=clubRequest.getClubName();
        this.clubCategory=clubRequest.getClubCategory();
        this.clubContent= clubRequest.getClubContent();
        this.clubMaximum = clubRequest.getClubMaximum();
        this.clubMinimum= clubRequest.getClubMinimum();
        this.clubTags = clubRequest.getClubTags();
        //this.clubPoint= new Point(clubRequest.getLatitude(), clubRequest.getLongitude());
        this.address= clubRequest.getAddress();
        this.meetingDate= clubRequest.getMeetingDate();
        this.masterId = clubRequest.getUserId();
        this.clubState=true;
    }


}



