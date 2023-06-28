package webProject.togetherPartyTonight.domain.club.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.club.entity.ClubSignup;

import java.util.List;

@Repository
public interface ClubSignupRepository extends JpaRepository<ClubSignup,Long> {
    List<ClubSignup> findAllByClubMemberId (Long clubSignupMemberId);
    List<ClubSignup> findAllByClubClubId (Long clubId);

    void deleteByClubClubId (Long clubId);

}
