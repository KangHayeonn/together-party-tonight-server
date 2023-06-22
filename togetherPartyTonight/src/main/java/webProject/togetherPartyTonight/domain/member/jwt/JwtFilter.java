package webProject.togetherPartyTonight.domain.member.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import webProject.togetherPartyTonight.global.common.ErrorResponse;
import webProject.togetherPartyTonight.global.error.ErrorCode;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        log.info("들어오는 경로 - {}", request.getServletPath());

        try {
            String token = jwtProvider.resolveToken(request);
            log.info("token 받아내기 - {}", token);
            if (token != null && jwtProvider.validateToken(token)) {

                Authentication auth = jwtProvider.getAuthentication(token); // 토큰을 통해서 인증 반납
                SecurityContextHolder.getContext().setAuthentication(auth); // 정상 토큰이면 SecurityContext에 저장
            }
            //token이 null이면 로그인 또는 회원가입 등등 토큰이 없는 경우를 고려해서 넘어간다 인증이 되지않는다.
            //그래서 SecurityConfig에서 permitAll이 되어있으면 요구하는 api로 ㄱㄱ 하고 없다면 403으로 접근권한 없다고 에러뿌린다.
        }catch (SignatureException  | MalformedJwtException | IllegalStateException e) {
            System.out.println("잘못된 토큰입니다.");
            //잘못된 토큰이 왔을때 접근권한 없다고 응답(잘못된 토큰) 403
            ErrorCode invalidTokenError = ErrorCode.FORBIDDEN;
            inValidTokenResponse(invalidTokenError,request,response,filterChain);
        }catch (ExpiredJwtException e){
            //토큰이 만료된 상황이다. 401
            ErrorCode invalidTokenError = ErrorCode.EXPIRED_TOKEN;
            inValidTokenResponse(invalidTokenError,request,response,filterChain);
        }

        filterChain.doFilter(request, response);
    }


    private void inValidTokenResponse(ErrorCode errorCode,HttpServletRequest request,HttpServletResponse response,FilterChain filterChain) throws IOException, ServletException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // 실패 메시지 작성
        ObjectMapper objectMapper = new ObjectMapper();
        ErrorResponse errorResponse = new ErrorResponse("false", errorCode);

        String result = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(result);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        //재발급에 관해서는 이 필터를 거치지않는다.
        return request.getServletPath().equals("/members/reissue");
    }
}
