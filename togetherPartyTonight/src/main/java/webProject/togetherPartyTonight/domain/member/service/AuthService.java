package webProject.togetherPartyTonight.domain.member.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webProject.togetherPartyTonight.domain.member.auth.MemberDetails;
import webProject.togetherPartyTonight.domain.member.dto.request.LoginRequestDto;
import webProject.togetherPartyTonight.domain.member.dto.request.ReissueRequestDto;
import webProject.togetherPartyTonight.domain.member.dto.response.LoginResponseDto;
import webProject.togetherPartyTonight.domain.member.dto.response.ReissueResponseDto;
import webProject.togetherPartyTonight.domain.member.exception.MemberException;
import webProject.togetherPartyTonight.domain.member.auth.jwt.JwtProvider;
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

    @Value("${spring.jwt.token.refresh-expiration-time}")
    private long refreshTokenExpireTime;

    public LoginResponseDto login(LoginRequestDto userLoginReqDto) {

        String email = userLoginReqDto.getEmail();
        String password = userLoginReqDto.getPassword();

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(email,password);

        //password를 비교하는 로직
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

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

    public ReissueResponseDto reissue(ReissueRequestDto reissueRequestDto){
        //토큰 재발급을 위한 로직

        String refreshToken = jwtProvider.resolveToken(reissueRequestDto.getRefreshToken());

        try{
            Authentication authentication = jwtProvider.getAuthentication(refreshToken);

            log.info("authentication정보 - {}",authentication);

            return new ReissueResponseDto(jwtProvider.createAccessToken(authentication),refreshToken);
        }catch (SignatureException e){
            throw new MemberException(ErrorCode.INVALID_TOKEN);
        }catch (IllegalArgumentException e){
            throw new MemberException(ErrorCode.FORBIDDEN);
        }catch (ExpiredJwtException e){
            throw new MemberException(ErrorCode.EXPIRED_TOKEN);
        }
    }

}