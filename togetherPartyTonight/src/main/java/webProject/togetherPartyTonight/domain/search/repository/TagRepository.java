package webProject.togetherPartyTonight.domain.search.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.search.entity.Tag;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag,Long> {

    List<Tag> findTop12ByOrderByTagCountDesc ();
}
