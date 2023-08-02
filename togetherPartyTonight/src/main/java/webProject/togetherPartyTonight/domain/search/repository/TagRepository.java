package webProject.togetherPartyTonight.domain.search.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.search.entity.Tag;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag,Long> {

    List<Tag> findTop12ByOrderByTagCountDesc ();

    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO tag (created_date, modified_date, tag_name, tag_count) " +
            "VALUES (NOW(), NOW(), :tagName, 1) " +
            "ON DUPLICATE KEY UPDATE tag_count = tag_count + 1;")
    void saveTagNameAndTagCount(@Param(value = "tagName")String tagName);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE tag SET tag_count= tag_count -1 WHERE tag_name = :tagName")
    void deleteTagCount(@Param(value = "tagName")String tagName);
}
