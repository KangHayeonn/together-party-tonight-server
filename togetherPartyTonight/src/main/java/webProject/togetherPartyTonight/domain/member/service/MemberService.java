package webProject.togetherPartyTonight.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import webProject.togetherPartyTonight.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

}
