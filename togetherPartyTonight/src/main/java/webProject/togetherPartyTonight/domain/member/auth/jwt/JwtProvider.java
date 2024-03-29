package webProject.togetherPartyTonight.domain.member.auth.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import webProject.togetherPartyTonight.domain.member.auth.MemberDetails;
import webProject.togetherPartyTonight.domain.member.dto.request.ReissueRequestDto;
import webProject.togetherPartyTonight.domain.member.entity.Member;
import webProject.togetherPartyTonight.domain.member.exception.MemberException;
import webProject.togetherPartyTonight.domain.member.exception.errorCode.MemberErrorCode;
import webProject.togetherPartyTonight.domain.member.repository.MemberRepository;
import webProject.togetherPartyTonight.domain.member.service.MemberDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {
    private final MemberRepository memberRepository;


    @Value("${spring.jwt.secret}")
    private String secretKey;

    @Value("${spring.jwt.token.access-expiration-time}")
    private long accessExpirationTime;

    @Value("${spring.jwt.token.refresh-expiration-time}")
    private long refreshExpirationTime;


    /**
     * Access 토큰 생성
     */
    public String createAccessToken(Authentication authentication){
        Claims claims = Jwts.claims().setSubject(String.valueOf(authentication.getName()));
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + accessExpirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * Refresh 토큰 생성
     */
    public String createRefreshToken(Authentication authentication){

        Claims claims = Jwts.claims().setSubject(authentication.getName());
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshExpirationTime);


        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();


        return refreshToken;
    }

    /**
     * 토큰으로부터 클레임을 만들고, 이를 통해 Authentication 객체 반환
     */
    public Authentication getAuthentication(String token) {

        String sub =  Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody().getSubject();

        Member member = memberRepository.findById(Long.parseLong(sub)).orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        MemberDetails memberDetails = new MemberDetails(member);
        return new UsernamePasswordAuthenticationToken(memberDetails, "",null);
    }

    /**
     * Bearer형식의 토큰을 순수한 토큰으로 바꾼다.
     */
    public String resolveToken(String token) {
        String bearerToken = token;

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }



    /**
     * Access 토큰을 검증
     */
    public boolean validateToken(String token) throws ExpiredJwtException,SignatureException{
        Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        return true;

    }
}
