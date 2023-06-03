package webProject.togetherPartyTonight.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.chat.entity.ChatHistory;

@Repository
public interface ChatHistoryRepository extends JpaRepository<ChatHistory,Long> {
}
