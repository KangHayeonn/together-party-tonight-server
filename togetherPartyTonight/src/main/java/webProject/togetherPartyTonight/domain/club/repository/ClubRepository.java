package webProject.togetherPartyTonight.domain.club.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.club.entity.Club;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<Club,Long> {

    List<Club> findClubByMasterId (Long masterId);

    Optional<Club> findByClubIdAndMasterId(Long clubId, Long masterId);

    @Query(nativeQuery = true, value = "UPDATE club SET club_state=false")
    void updateClubState ();


}
