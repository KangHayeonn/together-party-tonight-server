package webProject.togetherPartyTonight.domain.chat.service;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;


import java.io.IOException;
import java.util.*;


@Service
public class WebSocketService {

    // Redis Template 연동
    private RedisTemplate<String, Object> redisTemplate;
    String sessionKeyName = "sessionKeyMap";

    @Autowired
    public WebSocketService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addMember(WebSocketSession session) {

        Object result = redisGet(sessionKeyName);

        if (result == null) {
            Map<WebSocketSession, String> webSocketSessionStringMap = new HashMap<>();
            webSocketSessionStringMap.put(session, "");
            addStringKeyWithMapValue(sessionKeyName, webSocketSessionStringMap);
            return;
        }

        Map<WebSocketSession, String> resultMap = (Map<WebSocketSession, String>) result;
        resultMap.put(session,"");
        addStringKeyWithMapValue(sessionKeyName, resultMap);
    }

    public void removeMember(WebSocketSession session) {

        Object result = redisGet(sessionKeyName);

        if (result == null) {
            return;
        }

        Map<WebSocketSession, String> resultMap = (Map<WebSocketSession, String>) result;
        resultMap.remove(session);
        addStringKeyWithMapValue(sessionKeyName, resultMap);
    }

    public void redisAddStringValue(String key, Object value){
        ValueOperations<String, Object> stringStringValueOperations = redisTemplate.opsForValue();
        stringStringValueOperations.set(key, value);
    }


    // redis에서 Map을 추가하는 방법.
    public void addStringKeyWithMapValue(String key, Map<WebSocketSession, String> mapValue) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.putAll(key, mapValue);
    }

    public Object redisGet(String key) {
        Optional<ValueOperations<String, Object>> stringObjectValueOperations = Optional.of(redisTemplate.opsForValue());
        return stringObjectValueOperations.get().get(key);
    }

    public void broadcast(String receivedMessage) {

        // 받은 메시지를 다른 클라이언트들에게 전달

        Object result = redisGet(sessionKeyName);

        if (result == null) {
            return;
        }

        Map<WebSocketSession, String> resultMap = (Map<WebSocketSession, String>)result;
        resultMap.keySet().forEach(session-> {
            try {
                session.sendMessage(new TextMessage("Echo: " + receivedMessage));
            } catch (IOException e) {
                    e.printStackTrace();
            }
        });
    }
}
