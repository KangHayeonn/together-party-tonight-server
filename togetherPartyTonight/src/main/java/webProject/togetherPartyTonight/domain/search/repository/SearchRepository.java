package webProject.togetherPartyTonight.domain.search.repository;

import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.club.entity.Club;

import java.util.List;
import java.util.Optional;

@Repository
public interface SearchRepository extends JpaRepository<Club,Long> {

    String searchDefaultQuery = "SELECT club_id, c.created_date, c.modified_date, address," +
            "club_category, club_content, club_maximum, club_minimum, club_name,club_state," +
            "club_tags, club_image, club_meeting_date, club_master_id, ST_AsText(club_point) AS club_point " +
            "FROM club c ";
    String where = "WHERE ST_DISTANCE_SPHERE(ST_GEOMFROMTEXT(:standard_point,4326), club_point) <= :distance " +
            "AND club_maximum <= :userNum " +
            "AND :category LIKE CONCAT('%', club_category , '%') " +
            "AND club_tags REGEXP CONCAT('\\\\b',:tags,'\\\\b') " +
            "AND ((:state = 'recruit' AND club_state = 1) OR (:state = 'all')) ";


    @Query(nativeQuery = true, value = searchDefaultQuery+
            "WHERE ST_DISTANCE_SPHERE(ST_GEOMFROMTEXT(:standard_point,4326), club_point) <= 5000;" )
    Optional<List<Club>> findByAddress(@Param("standard_point") String point);


    @Query(nativeQuery = true, value = searchDefaultQuery+
            "JOIN member m ON c.club_master_id = m.member_id "
            +where +
            "ORDER BY m.member_rating_avg DESC;")
    Optional<List<Club>>  findByConditionsOrderByReviewScore (@Param("standard_point") String point, @Param("distance")Integer distance,
                                          @Param("state")String state, @Param("category")String category, @Param("userNum")Integer userNum,
                                          @Param("tags")String tags);


    @Query(nativeQuery = true, value = searchDefaultQuery+ where +
            "ORDER BY created_date DESC")
    Optional<List<Club>> findByConditionsOrderByDate (@Param("standard_point") String point, @Param("distance")Integer distance,
                                                      @Param("state")String state, @Param("category")String category, @Param("userNum")Integer userNum,
                                                      @Param("tags")String tags);


    /**
     * SELECT *
     * FROM club
     * WHERE ST_DISTANCE_SPHERE(ST_GEOMFROMTEXT('POINT (37.5592041015625 126.94230651855469)',4326), club_point) <= 10000
     *             AND club_maximum <= 20
     *             AND "맛집,취미" LIKE CONCAT("%", club_category , "%")
     *             AND club_tags REGEXP ('\\b신촌\\b')
     *             AND (('all' = 'recruit' AND club_state = 1) OR ('all' = 'all'))
     */
}
