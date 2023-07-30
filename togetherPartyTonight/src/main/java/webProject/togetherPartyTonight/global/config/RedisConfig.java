package webProject.togetherPartyTonight.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis 설정
 */
@Configuration
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;
    public RedisConnectionFactory redisConnectionFactory() {
        //레디스 비밀번호 설정
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setPassword(password);
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(host, port);
        // LettuceConnectionFactory를 구성하는 추가 설정 작성
        connectionFactory.afterPropertiesSet(); // 초기화 수행
        return connectionFactory;
    }

    /**
     *  redisTemplate 를 하나 만들어 두었습니다 타입에 따라 추가적으로 만드셔도됩니다.
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        // Redis 데이터 타입에 따라 직렬화/역직렬화 설정
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        //StringRedisSerialize 가 아닌 GenericToStringSerializer 을 해줘야 Value 에 Object 를 넣을 수 있음.
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));

        return redisTemplate;
    }
}
