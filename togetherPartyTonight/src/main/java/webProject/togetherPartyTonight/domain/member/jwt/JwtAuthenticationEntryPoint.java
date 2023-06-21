package webProject.togetherPartyTonight.domain.member.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import webProject.togetherPartyTonight.global.common.ErrorResponse;
import webProject.togetherPartyTonight.global.error.ErrorCode;

import javax.naming.AuthenticationNotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // filter에서 인증과 관련된 오류가 발생했을 때 여기를 거친다.

        //프로젝트 응답 규약으로 200을 응답으로 주기로 했다.
        response.setStatus(HttpStatus.SC_OK);

        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // 실패 메시지 작성
        ObjectMapper objectMapper = new ObjectMapper();
        ErrorResponse errorResponse = new ErrorResponse("false", ErrorCode.UNAUTHORIZED);

        String result = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(result);
    }
}
