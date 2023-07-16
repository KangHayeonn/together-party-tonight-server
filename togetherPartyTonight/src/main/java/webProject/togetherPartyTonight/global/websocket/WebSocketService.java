package webProject.togetherPartyTonight.global.websocket;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import webProject.togetherPartyTonight.domain.comment.dto.response.CreateCommentResponseDto;
import webProject.togetherPartyTonight.domain.comment.dto.response.GetCommentResponseDto;
import webProject.togetherPartyTonight.domain.comment.exception.CommentErrorCode;
import webProject.togetherPartyTonight.domain.comment.exception.CommentException;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.domain.member.repository.MemberRepository;
import webProject.togetherPartyTonight.global.common.service.RedisService;
import webProject.togetherPartyTonight.global.websocket.command.SocketLoginHandler;
import webProject.togetherPartyTonight.global.websocket.command.WebSocketHandler;
import webProject.togetherPartyTonight.global.websocket.socketMessage.CommonSocketRequestMessage;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 기본적으로 소켓 접속 시도 한 모든 소켓 접속에 대한 서비스를 반영합니다
 * 추후 공통 도메인으로 이동을 고려하겠습니다
 */
@Slf4j
@Service
public class WebSocketService {

    // Redis Service 사용
    private RedisService redisService;

    // MemberRepository 사용
    private MemberRepository memberRepository;


    //추후 config 관리를 염두해두기.
    private String sessionUserName = "sessionUserMap";
    private String userSessionName = "userSessionName";

    //멀티 쓰레드에서 사용 가능한 해시맵
    private Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    private Map<Long,Map<String, WebSocketSession>> commentSessionMap = new ConcurrentHashMap<>();

    //command 수행 manager 해시맵
    private HashMap<String/**command*/, WebSocketHandler /** commandHandler*/> commandHandler = new HashMap<>();

    @Autowired
    public WebSocketService(RedisService redisService, MemberRepository memberRepository) {
        this.redisService = redisService;
        this.memberRepository = memberRepository;
        commandHandler.put("login", new SocketLoginHandler(this));

    }

    public void addMember(WebSocketSession session) throws JsonProcessingException {

        String sessionId = session.getId();

        //sessionMap 에 없다면 삽입
        if (!sessionMap.containsKey(sessionId)) {
            sessionMap.put(sessionId, session);
        }
    }

    public void removeMember(WebSocketSession session) {
        sessionMap.remove(session.getId());
        logOut(session.getId());
    }

    //모든 세션에 전체 발송
    public void broadcastAll(String message, String sessionId) {

        // 받은 메시지를 다른 클라이언트들에게 전달
        sessionMap.forEach((key,session)-> {
            try {
                if (!session.isOpen()) {
                    removeMember(session);
                } else {
                    session.sendMessage(new TextMessage("sender :"+sessionId+", message: "+message));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void login(long userId, String sessionId) {
        memberRepository.findById(userId)
                .orElseThrow(()->{
                    log.info("[WebSocketService] login error user is Empty userId: {}", userId);
                    throw new RuntimeException();
                });
        //redis 에 양방향 저장
        redisService.addStringKeyWithMap(sessionUserName, sessionId, userId);
        redisService.addStringKeyWithMap(userSessionName, userId, sessionId);
    }

    public void logOut(String sessionId) {
        Long userId = (Long) redisService.getStringKeyWithMap(sessionUserName, sessionId);
        redisService.removeStringKeyWithMapKey(sessionUserName, sessionId);
        redisService.removeStringKeyWithMapKey(userSessionName, userId);
    }

    public void executeCommand(String receivedMessage, String sessionId) {
        CommonSocketRequestMessage commonSocketRequestMessage = new Gson().fromJson(receivedMessage, CommonSocketRequestMessage.class);
        WebSocketHandler webSocketHandler = commandHandler.get(commonSocketRequestMessage.getCommand());
        webSocketHandler.execute(receivedMessage, sessionId);
    }


    public boolean sendUser(long userId, String message) {
        try {
            String sessionId = (String) redisService.getStringKeyWithMap(userSessionName, userId);
            if (StringUtils.isEmpty(sessionId)) {
                return false;
            }
            if (!sessionMap.containsKey(sessionId)) {
                return false;
            }

            WebSocketSession webSocketSession = sessionMap.get(sessionId);
            webSocketSession.sendMessage(new TextMessage(message));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

        public void addMemberToCommentSession(Long clubId, Member member)  {

            String sessionId = (String)redisService.getStringKeyWithMap(userSessionName, member.getId());
            WebSocketSession webSocketSession = sessionMap.get(sessionId);

            //sessionMap 에 없다면 삽입
        if (!commentSessionMap.containsKey(clubId)) {
            ConcurrentHashMap<String, WebSocketSession> webSocketSessionMap = new ConcurrentHashMap<>();
            webSocketSessionMap.put(sessionId, webSocketSession);
            commentSessionMap.put(clubId, webSocketSessionMap);
        }
        else {
            Map<String, WebSocketSession> getSessionMap = commentSessionMap.get(clubId);
            getSessionMap.put(sessionId, webSocketSession);
        }
    }

    public void deleteMemberFromCommentSession(Long clubId, Member member) {
        String exitSessionId = (String)redisService.getStringKeyWithMap(userSessionName, member.getId());
        commentSessionMap.forEach((key,map)-> {
            if (clubId == key) {
                map.forEach((sessionId, value) -> {
                    if (sessionId.equals(exitSessionId)) {
                        map.remove(sessionId);
                    }
                });
            }
        });
    }

    public void broadcastComment(String message, Long clubId) {
        // 받은 메시지를 다른 클라이언트들에게 전달
        commentSessionMap.forEach((key,map)-> {
            if (clubId == key) {
                map.forEach((sessionId, value) -> {
                    try {
                        value.sendMessage(new TextMessage(message));
                    } catch (IOException e) {
                        throw new CommentException(CommentErrorCode.SOCKET_MESSAGE_PUB_FAIL);
                    }
                });
            }
        });
    }

}
