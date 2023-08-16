package webProject.togetherPartyTonight.domain.chat.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.chat.entity.Chat;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

//    @Query("SELECT c FROM Chat c WHERE c.chatRoom.id = :roomId AND c.id <= :maxChatId ORDER BY c.id DESC")
//    Optional<List<Chat>> findLatestChatsByRoomIdWithMaxChatId(@Param("roomId") Long roomId, @Param("maxChatId") Long maxChatId, Pageable pageable);

    Optional<List<Chat>> findByChatRoomIdAndIdLessThanOrderByIdDesc(Long roomId, Long maxChatId, Pageable pageable);

//    @Query("SELECT c FROM Chat c WHERE c.chatRoom.id = :roomId ORDER BY c.id DESC")
//    Optional<List<Chat>> findLatestChatsByRoomId(@Param("roomId") Long roomId, Pageable pageable);

    Optional<List<Chat>> findByChatRoomIdOrderByIdDesc(Long roomId, Pageable pageable);

    void deleteByChatRoomId(Long chatRoomId);

}
