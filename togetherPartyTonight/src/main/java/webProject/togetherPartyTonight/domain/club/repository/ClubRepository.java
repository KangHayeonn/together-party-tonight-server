package webProject.togetherPartyTonight.domain.club.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.club.entity.Club;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<Club,Long> {

    String searchDefaultQuery = "SELECT a.club_id, a.created_date, a.modified_date, a.address," +
            "a.club_category, a.club_content, a.club_maximum, a.club_name, a.club_state," +
            "a.club_tags, a.club_image, a.club_meeting_date, a.club_master_id, ST_AsText(a.club_point) AS club_point " +
            "FROM club a ";

    Optional<Page<Club>> findClubByMasterId (Long masterId, Pageable pageable);

    @Query(nativeQuery = true,
            value = searchDefaultQuery + "JOIN billing b ON a.club_id=b.club_id " +
            "WHERE a.club_master_id=:masterId")
    Optional<Page<Club>> findClubByMasterIdAndBillingStart (@Param(value = "masterId") Long masterId, Pageable pageable);

    @Query(nativeQuery = true,
            value = searchDefaultQuery + "LEFT JOIN billing b ON a.club_id=b.club_id " +
                    "WHERE a.club_master_id=:masterId AND b.club_id IS NULL")
    Optional<Page<Club>> findClubByMasterIdAndBillingNotStart (@Param(value = "masterId") Long masterId, Pageable pageable);

    Optional<Page<Club>> findClubByMasterIdAndClubState (Long masterId, Boolean clubState, Pageable pageable);

    Optional<Club> findByClubIdAndMasterId(Long clubId, Long masterId);

    @Query(nativeQuery = true, value = "UPDATE club SET club_state=false WHERE club_id = :clubId")
    @Modifying
    void updateClubState (@Param("clubId") Long clubId);


}
