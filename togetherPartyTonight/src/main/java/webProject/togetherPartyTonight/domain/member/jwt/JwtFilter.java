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

@Component
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
        }catch (SignatureException  | MalformedJwtException | IllegalArgumentException e) {
            //잘못된 토큰이 왔을때 인증이 필요하다고 응답(잘못된 토큰) 403
            ErrorCode invalidTokenError = ErrorCode.UNAUTHORIZED;
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





}
