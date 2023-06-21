package webProject.togetherPartyTonight.domain.chat.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import webProject.togetherPartyTonight.global.common.service.RedisService;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 기본적으로 소켓 접속 시도 한 모든 소켓 접속에 대한 서비스를 반영합니다
 * 추후 공통 도메인으로 이동을 고려하겠습니다
 */
@Service
public class WebSocketService {

    // Redis Service 사용
    private RedisService redisService;

    //추후 config 관리를 염두해두기.
    private String sessionKeyName = "sessionKeyMap";
    private ObjectMapper mapper = new ObjectMapper();

    //멀티 쓰레드에서 사용 가능한 해시맵
    private Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    @Autowired
    public WebSocketService(RedisService redisService) {
        this.redisService = redisService;
    }

    public void addMember(WebSocketSession session) throws JsonProcessingException {

        String sessionId = session.getId();

        //sessionMap 에 없다면 삽입
        if (!sessionMap.containsKey(sessionId)) {
            sessionMap.put(sessionId, session);
        }

        //이 부분은 추후 로그인 시에 넣어줘도 됨. value 에 userId 를 넣어두면 좋을 것 같음.
        redisService.addStringKeyWithMapKey(sessionKeyName, sessionId, null);
    }

    public void removeMember(WebSocketSession session) {
        redisService.removeStringKeyWithMapKey(sessionKeyName, session.getId());
    }

    //모든 세션에 전체 발송
    public void broadcastAll(String message, String sessionId) {

        // 받은 메시지를 다른 클라이언트들에게 전달
        sessionMap.forEach((key,value)-> {
            try {
                if (!value.isOpen()) {
                    removeMember(value);
                } else {
                    value.sendMessage(new TextMessage("sender :"+sessionId+", message: "+message));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
