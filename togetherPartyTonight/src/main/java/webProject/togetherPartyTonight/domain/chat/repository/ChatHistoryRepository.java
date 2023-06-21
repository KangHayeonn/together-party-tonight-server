package webProject.togetherPartyTonight.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.chat.entity.Chat;

@Repository
public interface ChatHistoryRepository extends JpaRepository<Chat,Long> {
}
