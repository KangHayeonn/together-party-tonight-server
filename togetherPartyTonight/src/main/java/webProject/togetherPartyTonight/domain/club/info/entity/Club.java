package webProject.togetherPartyTonight.domain.club.info.entity;


import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Point;
import webProject.togetherPartyTonight.domain.club.info.dto.AddClubRequest;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.global.common.BaseEntity;
import webProject.togetherPartyTonight.global.util.PointConverter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@Table(name = "club")
public class Club extends BaseEntity {
    @Id
    @Column(name = "club_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clubId;

    @ManyToOne
    @JoinColumn(name = "club_master_id", nullable = false)
    private Member master;

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

    @Column(name = "club_point", columnDefinition = "POINT")
    @ColumnTransformer(read = "ST_AsText(club_point)",write = "ST_GeomFromText(?, 4326)")
    @Convert(converter = PointConverter.class)
    private Point clubPoint;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "club_meeting_date", nullable = false)
    private LocalDate meetingDate;

    @Column(name = "club_state", nullable = false, length = 1, columnDefinition = "tinyint")
    private Boolean clubState; //true: 모집중, false: 모집 완료




}



