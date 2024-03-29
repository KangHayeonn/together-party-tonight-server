package webProject.togetherPartyTonight.domain.alert.entity;

import lombok.*;
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
@Table(name = "alert", indexes = {
        @Index(name = "member_id_id_idx", columnList = "member_id,id"),
        @Index(name = "member_id_alert_check_state_id_idx", columnList = "member_id,alert_check_state,id"),
})
public class Alert extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    private Member member;

    @Column(name = "alert_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private AlertType alertType;

    @Column(name = "alert_content", length = 1000, nullable = false)
    private String alertContent;

    @Column(name = "alert_check_state", nullable = false)
    private boolean alertCheckState;
}
