package webProject.togetherPartyTonight.domain.search.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import webProject.togetherPartyTonight.global.common.BaseEntity;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tag", indexes = {
        @Index(name = "tag_count_idx", columnList = "tag_count")
})
public class Tag extends BaseEntity {
    @Id
    @Column(name = "tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    @Column(name="tag_name", nullable = false, unique = true)
    private String tagName;

    @Column(name="tag_count", nullable = false)
    private Long tagCount;


}
