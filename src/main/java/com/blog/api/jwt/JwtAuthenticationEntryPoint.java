package com.blog.api.jwt;

import com.blog.api.response.ErrorResponse;
import com.blog.api.util.CustomError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        //유효한 자격증명을 제공하지 않고 접근하려 할 때 401
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .message("unauthorized")
                    .build();

            errorResponse.addError(CustomError.builder()
                    .message("로그인이 필요한 서비스입니다.")
                    .build());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(outputStream, errorResponse);
            outputStream.flush();
        }
    }
}
