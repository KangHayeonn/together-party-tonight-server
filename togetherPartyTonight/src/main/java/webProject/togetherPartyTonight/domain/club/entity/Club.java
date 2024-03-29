package webProject.togetherPartyTonight.domain.club.entity;


import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.DynamicInsert;
import org.locationtech.jts.geom.Point;
import webProject.togetherPartyTonight.domain.billing.entity.Billing;
import webProject.togetherPartyTonight.domain.chat.entity.ChatRoom;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.global.common.BaseEntity;
import webProject.togetherPartyTonight.global.util.PointConverter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "club", indexes = {
        @Index(name = "masterId_clubState", columnList = "club_master_id,club_state,club_id"),
        @Index(name = "masterId_clubId", columnList = "club_master_id,club_id"),
        @Index(name = "club_point_idx", columnList = "club_point"),
        @Index(name = "club_state_club_maximum_idx", columnList = "club_state,club_maximum"),
        @Index(name = "club_category_club_tags_idx", columnList = "club_category,club_tags")
})
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
    @Enumerated(EnumType.STRING)
    private ClubCategory clubCategory;

    @Column(name = "club_maximum",nullable = false, length = 11)
    private Integer clubMaximum;

    @Column(name = "club_content", nullable = false)
    private String clubContent;

    @Column(name = "club_tags", nullable = false)
    private String clubTags;

    @Column(name = "club_point", columnDefinition = "GEOMETRY", nullable = false)
    @ColumnTransformer(read = "ST_AsText(club_point)",write = "ST_GeomFromText(?, 4326)")
    @Convert(converter = PointConverter.class)
    private Point clubPoint;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "club_meeting_date", nullable = false)
    private LocalDateTime meetingDate;

    @Column(name = "club_state", nullable = false, length = 1, columnDefinition = "tinyint")
    private Boolean clubState; //true: 모집중, false: 모집 완료

    @Column(name = "club_image", nullable = false)
    private String image;

    @OneToMany(mappedBy = "club", fetch = LAZY)
    private List<ClubMember> clubMembers = new ArrayList<>();

    @OneToOne(mappedBy = "club", fetch = LAZY)
    private Billing billing;




}



