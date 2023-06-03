package webProject.togetherPartyTonight.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * redis 설정
 */
@Configuration
public class RedisConfig {
//    @Value("${spring.redis.host}")
//    private String host;
//    @Value("${spring.redis.port}")
//    private int port;

//    public RedisConnectionFactory redisConnectionFactory() {
//        return new LettuceConnectionFactory(host,port);
//    }

    /**
     * RedisRepository , RedisTemplate 중 선택하여 사용하면 됨
     */
}
