package webProject.togetherPartyTonight.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webProject.togetherPartyTonight.domain.member.dto.request.MemberInfoModifyDto;
import webProject.togetherPartyTonight.domain.member.dto.request.PasswordChangeDto;
import webProject.togetherPartyTonight.domain.member.dto.response.MemberInfoResponseDto;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.domain.member.exception.MemberException;
import webProject.togetherPartyTonight.domain.member.exception.errorCode.MemberErrorCode;
import webProject.togetherPartyTonight.domain.member.repository.MemberRepository;


@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public MemberInfoResponseDto findById(Long userId){

        Member member = getMember(userId);

        return MemberInfoResponseDto.from(member);

    }

    public void modifyMemberInfo(Long userId, MemberInfoModifyDto modifyDto){
        Member member = getMember(userId);

        member.setNickname(modifyDto.getNickname());
        member.setDetails(modifyDto.getMemberDetails());
        member.setProfileImage(modifyDto.getProfileImage());

        memberRepository.save(member);
    }

    public void changePassword(Long userId, PasswordChangeDto passwordChangeDto) {
        Member member = getMember(userId);
        boolean passwordMatch = passwordEncoder.matches(passwordChangeDto.getCurrentPassword(), member.getPassword());
        if(passwordMatch){
            //현재 패스워드와 변경 패스워드 같은 경우
            if(passwordChangeDto.getCurrentPassword().equals(passwordChangeDto.getNewPassword())){
                throw new MemberException(MemberErrorCode.PASSWORD_DUPLICATED);
            }
        }else{
            throw new MemberException(MemberErrorCode.INVALID_PASSWORD);
        }

        memberRepository.save(member);

    }

    private Member getMember(Long userId) {
        return memberRepository.findById(userId).orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }
}
