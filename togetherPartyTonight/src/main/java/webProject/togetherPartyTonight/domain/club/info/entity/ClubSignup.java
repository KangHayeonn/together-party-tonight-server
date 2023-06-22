package webProject.togetherPartyTonight.domain.club.info.entity;

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
@Table(name = "club_signup")
public class ClubSignup extends BaseEntity {
    @Id
    @Column(name = "club_signup_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clubSignupId;

    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @ManyToOne
    @JoinColumn(name = "club_signup_member_id", nullable = false)
    private Member clubMember;

    @ManyToOne
    @JoinColumn(name = "club_master_id", nullable = false)
    private Member clubMaster;

    @Column(name = "club_signup_approval_state", nullable = false)
    @Enumerated(EnumType.STRING)
    private ApprovalState clubSignupApprovalState;

    @Column(name = "club_signup_date", nullable = false)
    private LocalDateTime clubSignupDate;

    @Column(name = "club_signup_approval_date")
    private LocalDateTime clubSignupApprovalDate;




}
