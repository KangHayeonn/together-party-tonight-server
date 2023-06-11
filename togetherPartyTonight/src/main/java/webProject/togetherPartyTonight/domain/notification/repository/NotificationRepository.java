package webProject.togetherPartyTonight.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.notification.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
}
