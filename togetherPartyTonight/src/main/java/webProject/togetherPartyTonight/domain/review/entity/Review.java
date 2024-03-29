package webProject.togetherPartyTonight.domain.review.entity;

import lombok.*;
import webProject.togetherPartyTonight.domain.club.entity.Club;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.global.common.BaseEntity;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "review",
        indexes = {
                @Index(name="club_id_member_id_idx",columnList = "club_id, review_member_id"),
                @Index(name="club_id_idx",columnList = "club_id")})
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    //join column의 name: FK 이름
    //referencedColumnName이 대상테이블의 조인대상. 그러나 기본적으로 PK와 연결되기때문에 생략가능
    private Club club; //리뷰 대상

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_member_id",nullable = false)
    private Member member; //작성자

    @Column(name = "review_content", length = 300, nullable = false)
    private String reviewContent;

    @Column(name = "review_rating", columnDefinition = "TINYINT")
    private Integer rating;

    @Column(name = "review_image_url")
    private String imageUrl;






}
