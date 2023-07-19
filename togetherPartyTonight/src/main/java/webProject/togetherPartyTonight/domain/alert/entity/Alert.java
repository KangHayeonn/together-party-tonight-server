package webProject.togetherPartyTonight.domain.alert.entity;

import lombok.*;
import webProject.togetherPartyTonight.domain.billing.entity.BillingState;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.global.common.BaseEntity;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alert extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    private Member member;

    @Column(name = "alert_type", nullable = false, length = 255)
    @Enumerated(EnumType.STRING)
    private AlertType alertType;

    @Column(name = "alert_content", length = 1000, nullable = false)
    private String alert_content;

    @Column(name = "alert_check_state", nullable = false)
    private boolean alert_check_state;
}
