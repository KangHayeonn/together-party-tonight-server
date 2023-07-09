package webProject.togetherPartyTonight.domain.billing.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webProject.togetherPartyTonight.domain.club.entity.ClubMember;
import webProject.togetherPartyTonight.global.common.BaseEntity;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class BillingHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "billing_id")
    Billing billing;

    @ManyToOne
    @JoinColumn(name = "club_member_id")
    ClubMember clubMember;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "billing_state", nullable = false, length = 255)
    @Enumerated(EnumType.STRING)
    private BillingState billingState;
}
