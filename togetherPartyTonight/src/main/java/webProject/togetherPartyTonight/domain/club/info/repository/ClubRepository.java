package webProject.togetherPartyTonight.domain.club.info.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.club.info.entity.Club;

@Repository
public interface ClubRepository extends JpaRepository<Club,Long> {
}
