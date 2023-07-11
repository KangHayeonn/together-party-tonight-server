package webProject.togetherPartyTonight.domain.member.auth.mail;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MailServiceTest {

    @Autowired
    private MailService mailService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Test
    void sendMail() throws Exception {
        mailService.sendEmailForEmailAuth("cdw5713@naver.com",TitleType.SIGN_UP.getType());

        String s = redisTemplate.opsForValue().get("cdw5713@naver.com");

        log.info("인증번호 - {}", s);
    }
}