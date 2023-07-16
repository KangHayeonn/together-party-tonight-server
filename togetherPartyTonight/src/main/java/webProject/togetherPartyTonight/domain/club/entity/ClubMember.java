package webProject.togetherPartyTonight.domain.club.entity;

import lombok.*;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.global.common.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "club_member")
public class ClubMember extends BaseEntity {
    @Id
    @Column(name = "club_member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clubMemberId;

    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "club_join_date", nullable = false)
    private LocalDateTime clubJoinDate;

    public ClubMember (Club club, Member member) {
        this.club= club;
        this.member = member;
        this.clubJoinDate= LocalDateTime.now();
    }
}
