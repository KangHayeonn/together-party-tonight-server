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
import webProject.togetherPartyTonight.domain.member.dto.LoginRequestDto;
import webProject.togetherPartyTonight.domain.member.dto.RefreshTokenDto;
import webProject.togetherPartyTonight.domain.member.dto.TokenDto;
import webProject.togetherPartyTonight.domain.member.dto.TokenWithIdDto;
import webProject.togetherPartyTonight.domain.member.exception.MemberException;
import webProject.togetherPartyTonight.domain.member.jwt.JwtProvider;
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

    public TokenWithIdDto login(LoginRequestDto userLoginReqDto) {

        String email = userLoginReqDto.getEmail();
        String password = userLoginReqDto.getPassword();

        //아직 username,password 비교 안된 UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(email,password);

        //여기서 authenticate 메서드 호출될때 비교해서 인증 완료된 Authentication 뱉음
        //여기서 request들어온 이메일과 비밀번호를 db의 데이터와 비교하기 위해 loadByUsername등 온갖 메서드호출로 지지고 볶고 한다.
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        //id를 뽑기위한 principal(접근 주체) 얻어내기
        MemberDetails memberDetails = (MemberDetails)authentication.getPrincipal();

        String accessToken = jwtProvider.createAccessToken(authentication);
        String refreshToken = jwtProvider.createRefreshToken(authentication);
        TokenWithIdDto tokenWithIdDto = new TokenWithIdDto(
                    memberDetails.getId(),
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

        return tokenWithIdDto;
    }

    public TokenDto reissue(RefreshTokenDto refreshTokenDto){
        //토큰 재발급을 위한 로직
        System.out.println(refreshTokenDto.getRefreshToken());
        String refreshToken = jwtProvider.resolveToken(refreshTokenDto);

        try{
            Authentication authentication = jwtProvider.getAuthentication(refreshToken);
            log.info("authentication정보 - {}",authentication);
            log.info("pricipal정보 - {}",authentication.getName());
            return new TokenDto(
                    jwtProvider.createAccessToken(authentication),
                    jwtProvider.createRefreshToken(authentication)
            );
        }catch (SignatureException e){
            throw new MemberException(ErrorCode.INVALID_TOKEN);
        }catch (IllegalArgumentException e){
            throw new MemberException(ErrorCode.FORBIDDEN);
        }catch (ExpiredJwtException e){
            throw new MemberException(ErrorCode.EXPIRED_TOKEN);
        }
    }

}