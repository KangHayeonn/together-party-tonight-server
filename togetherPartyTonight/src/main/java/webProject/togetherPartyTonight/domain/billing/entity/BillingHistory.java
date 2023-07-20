package webProject.togetherPartyTonight.domain.billing.entity;

import lombok.*;
import webProject.togetherPartyTonight.domain.club.entity.ClubMember;
import webProject.togetherPartyTonight.global.common.BaseEntity;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillingHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "billing_id")
    Billing billing;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "club_member_id")
    ClubMember clubMember;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "billing_state", nullable = false, length = 255)
    @Enumerated(EnumType.STRING)
    private BillingState billingState;
}
