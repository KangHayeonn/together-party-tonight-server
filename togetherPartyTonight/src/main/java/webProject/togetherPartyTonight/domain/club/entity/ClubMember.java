package webProject.togetherPartyTonight.domain.club.entity;

import lombok.*;
import webProject.togetherPartyTonight.domain.billing.entity.Billing;
import webProject.togetherPartyTonight.domain.billing.entity.BillingHistory;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.global.common.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "club_member",
        indexes = {
        @Index(name = "club_id_member_id_idx", columnList = "club_id, member_id"),
        @Index(name = "club_id", columnList = "club_id")})
public class ClubMember extends BaseEntity {
    @Id
    @Column(name = "club_member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clubMemberId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "club_join_date", nullable = false)
    private LocalDateTime clubJoinDate;

    @OneToOne(mappedBy = "clubMember", fetch = LAZY)
    private BillingHistory billingHistory;

    public ClubMember (Club club, Member member) {
        this.club= club;
        this.member = member;
        this.clubJoinDate= LocalDateTime.now();
    }
}
