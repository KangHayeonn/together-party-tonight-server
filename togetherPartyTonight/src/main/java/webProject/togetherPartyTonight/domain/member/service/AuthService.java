package webProject.togetherPartyTonight.domain.member.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webProject.togetherPartyTonight.domain.member.auth.MemberDetails;
import webProject.togetherPartyTonight.domain.member.auth.jwt.JwtProvider;
import webProject.togetherPartyTonight.domain.member.dto.request.*;
import webProject.togetherPartyTonight.domain.member.dto.response.LoginResponseDto;
import webProject.togetherPartyTonight.domain.member.dto.response.ReissueResponseDto;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.domain.member.exception.MemberException;
import webProject.togetherPartyTonight.domain.member.exception.errorCode.AuthErrorCode;
import webProject.togetherPartyTonight.domain.member.exception.errorCode.MemberErrorCode;
import webProject.togetherPartyTonight.domain.member.exception.errorCode.TokenErrorCode;
import webProject.togetherPartyTonight.domain.member.repository.MemberRepository;
import webProject.togetherPartyTonight.global.error.ErrorCode;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    private final RedisTemplate<String, String> redisTemplate;

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${spring.jwt.token.refresh-expiration-time}")
    private long refreshTokenExpireTime;

    public void signup(SignupRequestDto signupRequestDto){


        String email = signupRequestDto.getEmail();
        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
        String nickname = signupRequestDto.getNickname();


        String authCode = redisTemplate.opsForValue().get(email);

        //인증번호 틀릴 시 오류 반환
        if(!signupRequestDto.getAuthCode().equals(authCode)) {
            throw new MemberException(AuthErrorCode.NOT_EQUAL_AUTH_CODE);
        }

        memberRepository.findMemberByEmailAndOauthProvider(email,null)
                .ifPresent((s)-> {
                   throw new MemberException(MemberErrorCode.MEMBER_ALREADY_EXIST);
                });

        memberRepository.save(
                Member.builder()
                        .email(email)
                        .password(encodedPassword)
                        .nickname(nickname)
                        .build()
        );
        //인증번호 레디스 삭제
        redisTemplate.delete(email);
    }

    public LoginResponseDto login(LoginRequestDto userLoginReqDto){

        String email = userLoginReqDto.getEmail();
        String password = userLoginReqDto.getPassword();

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(email,password);
        Authentication authentication = null;

        try{
            //password를 비교하는 로직
            authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        }catch(BadCredentialsException e){
            throw new MemberException(MemberErrorCode.INVALID_PASSWORD);
        }


        MemberDetails memberDetails = (MemberDetails)authentication.getPrincipal();

        String accessToken = jwtProvider.createAccessToken(authentication);
        String refreshToken = jwtProvider.createRefreshToken(authentication);
        LoginResponseDto loginResponseDto = new LoginResponseDto(
                    memberDetails.getMember().getId(),
                    accessToken,
                    refreshToken
                );

        // redis에 저장
        redisTemplate.opsForValue().set(
                authentication.getName(),
                refreshToken,
                refreshTokenExpireTime,
                TimeUnit.MILLISECONDS
        );

        return loginResponseDto;
    }

    public void resetPassword(PasswordResetRequestDto resetRequestDto){
        Member member = memberRepository.findMemberByEmailAndOauthProvider(resetRequestDto.getEmail(), null).orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        //인증번호 같지않을 시
        if(!resetRequestDto.getAuthCode().equals(redisTemplate.opsForValue().get(resetRequestDto.getEmail()))){
            throw new MemberException(AuthErrorCode.NOT_EQUAL_AUTH_CODE);
        }


        member.setPassword(passwordEncoder.encode(resetRequestDto.getNewPassword()));

        //레디스 인증번호 삭제
        redisTemplate.delete(resetRequestDto.getEmail());
    }

    public ReissueResponseDto reissue(ReissueRequestDto reissueRequestDto){
        //토큰 재발급을 위한 로직

        String refreshToken = jwtProvider.resolveToken(reissueRequestDto.getRefreshToken());

        try{
            Authentication authentication = jwtProvider.getAuthentication(refreshToken);

            log.info("authentication정보 - {}",authentication);

            return new ReissueResponseDto(jwtProvider.createAccessToken(authentication),refreshToken);
        }catch (SignatureException e){
            throw new MemberException(TokenErrorCode.INVALID_TOKEN);
        }catch (IllegalArgumentException e){
            throw new MemberException(ErrorCode.FORBIDDEN);
        }catch (ExpiredJwtException e){
            throw new MemberException(TokenErrorCode.EXPIRED_TOKEN);
        }
    }

    public void checkDuplicateEmail(EmailCheckRequestDto emailCheckRequestDto) {
        memberRepository.findMemberByEmailAndOauthProvider(emailCheckRequestDto.getEmail(), null)
                .ifPresent((s)->{
                    throw new MemberException(MemberErrorCode.MEMBER_ALREADY_EXIST);
                });
    }

    public void logout(Long memberId){
        memberRepository.findById(memberId).orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        try{
            //로그아웃 시 리프레시 토큰 삭제
            redisTemplate.delete(String.valueOf(memberId));
        }catch(Exception e){
            e.printStackTrace();
            throw new MemberException(MemberErrorCode.FAILED_LOGOUT);
        }
    }
}