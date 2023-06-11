package webProject.togetherPartyTonight.domain.club.clubComment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.club.clubComment.entity.ClubComment;

@Repository
public interface ClubCommentRepository extends JpaRepository<ClubComment,Long> {
}
