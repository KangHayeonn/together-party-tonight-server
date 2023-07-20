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


    Optional<Page<Club>> findClubByMasterId (Long masterId, Pageable pageable);

    Optional<Page<Club>> findClubByMasterIdAndClubState (Long masterId, Boolean clubState, Pageable pageable);

    Optional<Club> findByClubIdAndMasterId(Long clubId, Long masterId);

    @Query(nativeQuery = true, value = "UPDATE club SET club_state=false WHERE club_id = :clubId")
    @Modifying
    void updateClubState (@Param("clubId") Long clubId);


}
