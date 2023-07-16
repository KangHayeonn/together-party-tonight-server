package webProject.togetherPartyTonight.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import webProject.togetherPartyTonight.domain.member.auth.MemberDetails;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.domain.member.exception.MemberException;
import webProject.togetherPartyTonight.domain.member.exception.errorCode.MemberErrorCode;
import webProject.togetherPartyTonight.domain.member.repository.MemberRepository;
import webProject.togetherPartyTonight.global.error.ErrorCode;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        log.info("loadByUserName - {}",email);
        Member member = memberRepository.findMemberByEmailAndOauthProvider(email,null).orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        return new MemberDetails(member);


    }
}
