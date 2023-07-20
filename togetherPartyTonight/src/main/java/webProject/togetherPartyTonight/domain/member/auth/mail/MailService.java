package webProject.togetherPartyTonight.domain.member.auth.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import webProject.togetherPartyTonight.domain.member.exception.MemberException;
import webProject.togetherPartyTonight.domain.member.exception.errorCode.AuthErrorCode;
import webProject.togetherPartyTonight.domain.member.exception.errorCode.MemberErrorCode;
import webProject.togetherPartyTonight.domain.member.repository.MemberRepository;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MailService {

    private final MemberRepository memberRepository;

    private final JavaMailSender emailSender;

    private final SpringTemplateEngine templateEngine;

    private final RedisTemplate<String,String> redisTemplate;

    public void sendEmailForEmailAuth(String email,String title) {

        if(title.equals(TitleType.FOR_FIND_PASSWORD.getType())){
            //비밀번호 찾기 시 유저가 없으면 에러 응답
            memberRepository.findMemberByEmailAndOauthProvider(email,null).orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        }

        try{
            String code = createCode(); // 인증코드 생성


            MimeMessage message = emailSender.createMimeMessage();
            message.addRecipients(MimeMessage.RecipientType.TO, email); // 보낼 이메일 설정
            message.setSubject(title); // 이메일 제목
            message.setFrom(new InternetAddress("topato@topato.site","투바투"));  // 보낼 때 이름 설정하고 싶은 경우
            message.setText(setContext(code), "utf-8", "html"); // 내용 설정(Template Process)


            emailSender.send(message); // 이메일 전송

            redisTemplate.opsForValue()
                    .set(email,code,180000L, TimeUnit.MILLISECONDS); //3분의 유효시간
            log.info("레디스 저장 정보 - {}",redisTemplate.opsForValue().get(email));


        }catch (Exception e){
            log.error("이메일 전송에 실패했습니다.");
            throw new MemberException(AuthErrorCode.NOT_EMAIL_SEND);
        }

    }

    private String setContext(String code) { // 타임리프 설정하는 코드
        Context context = new Context();
        context.setVariable("code", code); // Template에 전달할 데이터 설정
        return templateEngine.process("emailAuth", context); //
    }

    private String createCode() {
        StringBuilder code = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 7; i++) {
            int rIndex = rnd.nextInt(3);
            switch (rIndex) {
                case 0:
                    code.append((char) (rnd.nextInt(26) + 97));
                    break;
                case 1:
                    code.append((char) (rnd.nextInt(26) + 65));
                    break;
                case 2:
                    code.append((rnd.nextInt(10)));
                    break;
            }
        }
        return code.toString();
    }
}
