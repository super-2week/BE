package com.supercoding.commerce03.service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supercoding.commerce03.service.user.exception.UserErrorCode;
import com.supercoding.commerce03.service.user.exception.UserErrorResponse;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


@Component//빈 등록
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    //토큰에 대한 에러가 내려온 마지막 남은 에러들을 처리 해줌
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException)
        throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);//응답 값을 json으로
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//401 에러 코드를 보내줌
        response.setCharacterEncoding("UTF-8");

        OutputStream outputStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper(); //응답 출력 스트림을 얻어와 ObjectMapper 생성

        //에러코드를 가져와야 함
        ResponseEntity<UserErrorResponse> body = ResponseEntity.status(UserErrorCode.HANDLE_ACCESS_DENIED.getHttpStatus())
            .body(UserErrorResponse.builder()
                .errorCode(UserErrorCode.HANDLE_ACCESS_DENIED)
                .errorMessage(UserErrorCode.HANDLE_ACCESS_DENIED.getDescription())
                .build());

        //출력스트림을 클라이언트로 전송
        mapper.writeValue(outputStream, body);
        outputStream.flush();

    }
}
