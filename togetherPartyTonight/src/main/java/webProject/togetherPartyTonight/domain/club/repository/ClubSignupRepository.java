package webProject.togetherPartyTonight.domain.club.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.club.entity.ClubSignup;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubSignupRepository extends JpaRepository<ClubSignup,Long> {
    List<ClubSignup> findAllByClubMemberId (Long clubSignupMemberId);

    @Query(nativeQuery = true, value = "SELECT * FROM club_signup WHERE club_id = :clubId AND " +
            "(club_signup_approval_state LIKE 'APPROVE' OR club_signup_approval_state LIKE 'PENDING');")
    List<ClubSignup> findAllByClubClubId (@Param("clubId") Long clubId);

    void deleteByClubClubId (Long clubId);

    Optional<ClubSignup> findByClubClubIdAndClubMemberId (Long clubId, Long memberId);

}
