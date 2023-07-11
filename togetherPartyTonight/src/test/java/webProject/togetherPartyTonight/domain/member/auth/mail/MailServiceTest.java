package webProject.togetherPartyTonight.domain.member.auth.mail;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import webProject.togetherPartyTonight.domain.member.exception.MemberException;

@Slf4j
@SpringBootTest
class MailServiceTest {

    @Autowired
    private MailService mailService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Test
    void sendMail() throws Exception {
        mailService.sendEmailForEmailAuth("cdw5713@naver.com",TitleType.FOR_FIND_PASSWORD.getType());

        String s = redisTemplate.opsForValue().get("cdw5713@naver.com");

        log.info("인증번호 - {}", s);
    }

    @Test
    void sendMailNotExist() throws Exception{
        //없는 회원에 대한 이메일 전송

        Assertions.assertThrows(MemberException.class, () ->mailService.sendEmailForEmailAuth("cdw5713@avlha.cahk",TitleType.FOR_FIND_PASSWORD.getType()));
    }
}