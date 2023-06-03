package webProject.togetherPartyTonight.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.review.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
}
