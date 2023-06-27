package webProject.togetherPartyTonight.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webProject.togetherPartyTonight.domain.chat.entity.ChatRoom;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<List<ChatRoom>> findByChatMemberAIdAndChatMemberBId(Long chatMemberAId, Long chatMemberBId);

}
