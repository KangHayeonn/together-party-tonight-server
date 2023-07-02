package webProject.togetherPartyTonight.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import webProject.togetherPartyTonight.domain.member.auth.jwt.JwtProvider;
import webProject.togetherPartyTonight.domain.member.dto.response.LoginResponseDto;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.domain.member.exception.MemberException;
import webProject.togetherPartyTonight.domain.member.oauth.OAuthInfoResponse;
import webProject.togetherPartyTonight.domain.member.oauth.OAuthLoginParam;
import webProject.togetherPartyTonight.domain.member.repository.MemberRepository;
import webProject.togetherPartyTonight.global.error.ErrorCode;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SocialLoginService {

    private final MemberRepository memberRepository;

    private final JwtProvider jwtProvider;

    private final OAuthUserInfoService oAuthUserInfoService;

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${spring.jwt.token.refresh-expiration-time}")
    private long refreshTokenExpireTime;


    public LoginResponseDto login(OAuthLoginParam loginParam){
        try{
            OAuthInfoResponse userInfo = oAuthUserInfoService.request(loginParam);
            Long memberId = findOrCreateMember(userInfo);
            Authentication authentication = new UsernamePasswordAuthenticationToken(memberId,null);

            String accessToken = jwtProvider.createAccessToken(authentication);
            String refreshToken = jwtProvider.createRefreshToken(authentication);
            LoginResponseDto responseDto = new LoginResponseDto(memberId, accessToken, refreshToken);


            // redis에 저장
            redisTemplate.opsForValue().set(
                    authentication.getName(),
                    refreshToken,
                    refreshTokenExpireTime,
                    TimeUnit.MILLISECONDS
            );

            return responseDto;
        }catch (RestClientException e){
            throw new MemberException(ErrorCode.INTERNAL_SERVER_ERROR);
        }


    }

    private Long findOrCreateMember(OAuthInfoResponse userInfo) {
        return memberRepository.findMemberByEmailAndOauthProvider(userInfo.getEmail(),userInfo.getOAuthProvider())
                .map(Member::getId)
                .orElseGet(() -> newMember(userInfo));
    }

    private Long newMember(OAuthInfoResponse userInfo) {
        Member newMember = Member.builder()
                .email(userInfo.getEmail())
                .profileImage(userInfo.getProfileImage())
                .nickname(userInfo.getNickname())
                .oAuthProvider(userInfo.getOAuthProvider())
                .build();

        return memberRepository.save(newMember).getId();
    }
}
