package webProject.togetherPartyTonight.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import webProject.togetherPartyTonight.domain.member.dto.MemberInfoDto;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.domain.member.exception.MemberException;
import webProject.togetherPartyTonight.domain.member.repository.MemberRepository;
import webProject.togetherPartyTonight.global.error.ErrorCode;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberInfoDto findById(Long userId){
        Member member = memberRepository.findById(userId).orElseThrow(() -> new MemberException(ErrorCode.INVALID_MEMBER_ID));
        System.out.println("여기 오긴함?");
        return MemberInfoDto.from(member);

    }
}
