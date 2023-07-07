package webProject.togetherPartyTonight.domain.member.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import webProject.togetherPartyTonight.domain.member.dto.request.SignupRequestDto;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.domain.member.repository.MemberRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    SignupRequestDto signupRequestDto;

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @BeforeEach
    void makeUser(){

        signupRequestDto = new SignupRequestDto();
        signupRequestDto.setEmail("cdw5713@naver.co");
        signupRequestDto.setPassword("7788459");
    }

    @Test
    void signup() {
        String password = signupRequestDto.getPassword();
        String email = signupRequestDto.getEmail();
        String encode = passwordEncoder.encode(password);

        Member member = Member.builder()
                .email(email)
                .password(encode)
                .nickname("대웅이")
                .build();
        Member saveMember = memberRepository.save(member);

        Authentication authentication = new UsernamePasswordAuthenticationToken(saveMember.getEmail(),password);
        Authentication authenticate = authenticationManager.authenticate(authentication);

        assertTrue(authenticate.isAuthenticated());
    }

    @Test
    void validPasswordEmail(){
        SignupRequestDto signupRequestDto = new SignupRequestDto();
        signupRequestDto.setEmail("cdw5713@naver");
        signupRequestDto.setPassword("a7788459!");
        signupRequestDto.setNickname("대웅이");
        // 유효성 검사 실행
        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(signupRequestDto);

        // 검사 결과 확인
        assertEquals(0, violations.size());

        violations.forEach(System.out::println);

    }
}