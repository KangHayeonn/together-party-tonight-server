package webProject.togetherPartyTonight.domain.billing.entity;

import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webProject.togetherPartyTonight.domain.club.entity.Club;
import webProject.togetherPartyTonight.global.common.BaseEntity;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Billing extends BaseEntity {

    @Id
    @Column(name = "billing_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @Column(name = "price", nullable = false)
    private Integer price;
}
