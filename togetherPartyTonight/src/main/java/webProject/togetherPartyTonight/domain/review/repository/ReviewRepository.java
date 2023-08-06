package webProject.togetherPartyTonight.domain.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.review.entity.Review;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {

    Optional<Review> findByClubClubIdAndMemberId (Long ClubId, Long memberId);
    Optional<Page<Review>> findByClubClubId (Long ClubId, Pageable pageable);
    Optional<Page<Review>> findByMemberId (Long memberId, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT CASE WHEN SUM(review_rating) IS NULL THEN 0 ELSE SUM(review_rating) END " +
            "FROM review WHERE club_id=:clubId")
    Long getSumByClubId (@Param(value = "clubId") Long clubId);

    Optional<Page<Review>> findByClubMasterId (Long masterId, Pageable pageable);
}
