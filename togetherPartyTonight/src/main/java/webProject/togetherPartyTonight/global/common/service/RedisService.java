package webProject.togetherPartyTonight.global.common.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RedisService {
    // Redis Template 연동
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //String key 에 Object 추가 일반적 Value 추가.
    public void addStringKeyWithObject(String key, Object value) {
        ValueOperations<String, Object> stringStringValueOperations = redisTemplate.opsForValue();
        stringStringValueOperations.set(key, value);
    }

    // String key 에 Map 을 한번에 추가
    public void addStringKeyWithMap(String key, Map<String, String> mapValue) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.putAll(key, mapValue);
    }

    // redis key 에 맵 형식으로 데이터를 넣는 것.
    public void addStringKeyWithMapKey(String key, String mapKey, Object mapValue) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.put(key, mapKey, mapValue);
    }

    // redis key 에 맵 형식으로 데이터를 삭제하는것.
    public void removeStringKeyWithMapKey(String key, String mapKey) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.delete(key, mapKey);
    }

    // String key 로 object 를 가져옴.  일반적인 Value
    public Object get(String key) {
        ValueOperations<String, Object> stringObjectValueOperations = redisTemplate.opsForValue();
        return stringObjectValueOperations.get(key);
    }
}
