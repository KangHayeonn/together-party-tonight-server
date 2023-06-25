package webProject.togetherPartyTonight.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import webProject.togetherPartyTonight.domain.chat.exception.ChatErrorCode;
import webProject.togetherPartyTonight.domain.chat.exception.ChatException;

@Service
@RequiredArgsConstructor
public class ChatService {

    public void exceptionTest() {

        throw new ChatException(ChatErrorCode.ALREADY_CHATROOM_EXIST);
    }
}
