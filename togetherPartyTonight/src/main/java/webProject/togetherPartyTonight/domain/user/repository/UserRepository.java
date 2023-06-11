package webProject.togetherPartyTonight.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
