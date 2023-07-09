package webProject.togetherPartyTonight.domain.member.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import webProject.togetherPartyTonight.domain.member.dto.request.MemberInfoModifyDto;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.domain.member.exception.MemberException;
import webProject.togetherPartyTonight.domain.member.exception.errorCode.MemberErrorCode;
import webProject.togetherPartyTonight.domain.member.repository.MemberRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberService memberService;


    @Test
    void modifyNickname() {
        Optional<Member> memberByEmailAndOauthProvider = memberRepository.findMemberByEmailAndOauthProvider("cdw5713@hanmail.net", null);

        Member member = memberByEmailAndOauthProvider.get();

        log.info("변경하기 전 닉네임 - {}",member.getNickname());

        member.setNickname("최대웅");

        log.info("변경 한후의 닉네임 - {}", member.getNickname());

    }

    @Test
    void modifyMemberInfo(){
        Member member = memberRepository.findById(6L).orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        MemberInfoModifyDto modifyDto = new MemberInfoModifyDto();
        modifyDto.setCurrentPassword("a12345678!");
        modifyDto.setNewPassword("12345");


        assertThrows(MemberException.class, () -> memberService.modifyMemberInfo(6L, modifyDto));
    }
}