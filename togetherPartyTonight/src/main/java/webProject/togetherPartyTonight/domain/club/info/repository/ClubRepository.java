package webProject.togetherPartyTonight.domain.club.info.repository;


import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.club.info.entity.Club;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club,Long> {

    String insertPointQuery = "UPDATE club SET club_point = ST_GEOMFROMTEXT(:point, 4326) WHERE club_id=:clubId";

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = insertPointQuery)
    int savePoint(@Param(value = "point") Point point, @Param(value = "clubId") Long clubId);

    List<Club> findClubByMasterId (Long masterId);


}
