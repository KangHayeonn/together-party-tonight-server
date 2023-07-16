package webProject.togetherPartyTonight.domain.club.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.club.entity.ClubMember;

import java.util.Optional;

@Repository
public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM club_member WHERE club_id = :clubId")
    int getMemberCnt (@Param(value = "clubId") Long clubId);

    void deleteByClubClubId (Long clubId);

    void deleteByMemberId (Long memberId);

    void deleteByClubClubIdAndMemberId(Long clubId, Long memberId);

    Optional<ClubMember> findByClubClubIdAndMemberId(Long clubId, Long memberId);
}
