package wisoft.labservice.domain.auth.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;
import wisoft.labservice.domain.common.dto.ApiResponse;

@Component
public class CustomSecurityExceptionHandler implements AccessDeniedHandler, AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 403 접근 권한 없음
     */
    @Override
    public void handle(final HttpServletRequest request, final HttpServletResponse response,
                       final AccessDeniedException accessDeniedException) throws IOException, ServletException {
        sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "접근 권한이 없습니다");
    }

    /**
     * 401 인증 필요
     */
    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException, ServletException {
        sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "인증이 필요합니다");
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException{
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");

        ApiResponse<Map<String, String>> errorResponse = new ApiResponse<>(
                false,
                Collections.singletonList(Map.of("message", message))
        );
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
