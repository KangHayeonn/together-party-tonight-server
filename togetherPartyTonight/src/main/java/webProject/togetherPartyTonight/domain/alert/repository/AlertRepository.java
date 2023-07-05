package webProject.togetherPartyTonight.domain.alert.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.alert.entity.Alert;

@Repository
public interface AlertRepository extends JpaRepository<Alert,Long> {
}
