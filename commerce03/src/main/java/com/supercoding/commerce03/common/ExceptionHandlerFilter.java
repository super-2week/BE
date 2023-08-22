package com.supercoding.commerce03.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supercoding.commerce03.service.user.exception.UserErrorCode;
import com.supercoding.commerce03.service.user.exception.UserErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.filter.OncePerRequestFilter;

public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        }catch (ExpiredJwtException e){
            setErrorResponse(response, UserErrorCode.EXPIRED_TOKEN);
        }catch (JwtException e){
            filterChain.doFilter(request,response);//요청을 계속 진행시키겠다
        }
    }
    private <T> void setErrorResponse(HttpServletResponse response, UserErrorCode userErrorCode) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(userErrorCode.getHttpStatus().value());//http상태 값을 int 값으로 변환해서 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ResponseEntity<UserErrorResponse> body = ResponseEntity.status(
                userErrorCode.getHttpStatus())
            .body(UserErrorResponse.builder()
                .errorCode(userErrorCode)
                .errorMessage(userErrorCode.getDescription())
                .build());
        try {
            response.getWriter().write(objectMapper.writeValueAsString(body));//출력 스트림을 얻어와 서버가 클라이언트로 데이터를 보낼 수 있음
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
