package webProject.togetherPartyTonight.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import webProject.togetherPartyTonight.domain.chat.service.WebSocketService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@Slf4j
public class ChatController {

    private WebSocketService webSocketService;


    @Autowired
    public ChatController(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }


}
