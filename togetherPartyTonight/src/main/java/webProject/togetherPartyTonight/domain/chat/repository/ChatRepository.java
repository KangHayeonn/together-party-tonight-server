package webProject.togetherPartyTonight.domain.chat.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.chat.entity.Chat;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<List<Chat>> findByChatRoomIdAndIdLessThanOrderByIdDesc(Long roomId, Long maxChatId, Pageable pageable);

    Optional<List<Chat>> findByChatRoomIdOrderByIdDesc(Long roomId, Pageable pageable);

    void deleteByChatRoomId(Long chatRoomId);
}
