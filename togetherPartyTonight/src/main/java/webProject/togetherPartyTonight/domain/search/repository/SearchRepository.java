package webProject.togetherPartyTonight.domain.search.repository;

import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
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
    String OptionWhere = "WHERE ST_DISTANCE_SPHERE(ST_GEOMFROMTEXT(:standard_point,4326), club_point) <= :distance " +
            "AND club_maximum <= :memberNum " +
            "AND :category LIKE CONCAT('%', club_category , '%') " +
            "AND club_tags REGEXP CONCAT('\\\\b',:tags,'\\\\b') " +
            "AND ((:state = 'recruit' AND club_state = 1) OR (:state = 'all')) ";

    String noOptionWhere = "WHERE ST_DISTANCE_SPHERE(ST_GEOMFROMTEXT(:standard_point,4326), club_point) <= 5000 ";

    @Query(nativeQuery = true, value = searchDefaultQuery+ noOptionWhere)
    Optional<Page<Club>> findByAddress(@Param("standard_point") String point, Pageable pageable);


    @Query(nativeQuery = true, value = searchDefaultQuery+
            "JOIN member m ON c.club_master_id = m.member_id "
            +OptionWhere +
            "ORDER BY m.member_rating_avg DESC ")
    Optional<Page<Club>>  findByConditionsOrderByReviewScore (@Param("standard_point") String point, @Param("distance")Integer distance,
                                          @Param("state")String state, @Param("category")String category, @Param("memberNum")Integer memberNum,
                                          @Param("tags")String tags, Pageable pageable);


    @Query(nativeQuery = true, value = searchDefaultQuery+ OptionWhere +
            "ORDER BY created_date DESC")
    Optional<Page<Club>> findByConditionsOrderByDate (@Param("standard_point") String point, @Param("distance")Integer distance,
                                                      @Param("state")String state, @Param("category")String category, @Param("memberNum")Integer memberNum,
                                                      @Param("tags")String tags, Pageable pageable);


}
