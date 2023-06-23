package webProject.togetherPartyTonight.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.comment.entity.ClubComment;

@Repository
public interface ClubCommentRepository extends JpaRepository<ClubComment,Long> {
}
