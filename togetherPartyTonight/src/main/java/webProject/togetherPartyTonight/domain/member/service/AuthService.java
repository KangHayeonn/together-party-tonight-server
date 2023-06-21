package webProject.togetherPartyTonight.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webProject.togetherPartyTonight.domain.member.auth.MemberDetails;
import webProject.togetherPartyTonight.domain.member.dto.LoginRequestDto;
import webProject.togetherPartyTonight.domain.member.dto.TokenDto;
import webProject.togetherPartyTonight.domain.member.dto.TokenWithIdDto;
import webProject.togetherPartyTonight.domain.member.jwt.JwtProvider;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AuthService {


    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public TokenWithIdDto login(LoginRequestDto userLoginReqDto) {

        System.out.println(userLoginReqDto.getEmail());
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

        TokenWithIdDto tokenWithIdDto = new TokenWithIdDto(
                    memberDetails.getId(),
                    jwtProvider.createAccessToken(authentication),
                    jwtProvider.createRefreshToken(authentication)
                );

        return tokenWithIdDto;
    }

    public TokenDto reissue(String refreshToken){
        //토큰 재발급을 위한 로직
        Authentication authentication = jwtProvider.getAuthentication(refreshToken);
        log.info("pricipal정보 -{}",authentication.getName());
        System.out.println(authentication.getPrincipal());

        return new TokenDto(
                jwtProvider.createAccessToken(authentication),
                jwtProvider.createRefreshToken(authentication)
        );
    }

}