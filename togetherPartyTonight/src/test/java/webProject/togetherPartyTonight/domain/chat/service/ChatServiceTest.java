package webProject.togetherPartyTonight.domain.chat.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import webProject.togetherPartyTonight.domain.chat.repository.ChatRepository;

import java.time.LocalDateTime;


@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatRepository chatRepository;

    @InjectMocks
    private ChatService chatService;

    @Test
    void createChatRoom() {
    }

    @Test
    void getChatRoomList() {
    }

    @Test
    void getChatLogList() {
    }

    @Test
    void sendChat() {
    }
}