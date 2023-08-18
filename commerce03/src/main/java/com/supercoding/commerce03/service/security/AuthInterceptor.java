package com.supercoding.commerce03.service.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Component
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {//현재 요청을 처리하는 메서드가 HandlerMethod가 맞는지 확인
            HandlerMethod handlerMethod = (HandlerMethod) handler;//해당 핸들러의 메서드 정보를 가져옴
            Auth authAnnotation = handlerMethod.getMethodAnnotation(Auth.class);//@Auth가 잘 적용되어있는지 확인하고 어노테이션을 가져온다

            if (authAnnotation != null) {//@Auth가 적용 되어있는 경우
                boolean includeUserId = authAnnotation.includeUserId();//회원의 id를 품고있는지
                log.info("잡았다 {}", includeUserId);
                if (includeUserId) {//참인 경우
                    if (!request.getHeader("Authorization")//
                        .startsWith("Bearer ")) { //헤더에 토큰 값이 올바르지 않은 경우
                        throw new RuntimeException("다시 로그인을 해야 합니다");//다시 로그인 하라는 메세지
                    }
                    String token = request.getHeader("Authorization").split(" ")[1];//헤더에서 토큰 값을 추출해온다
                    //올바른 토큰인지 확인

                    Long userId = JwtTokenProvider.getLoginId(token);//추출해 가져온 토큰을 이용해 아이디를 가져오기
                    request.setAttribute("userId", userId);//요청 값에 userId라는 이름으로 인덱스 값 설정
                    AuthHolder.setUserId(userId);//AuthHolder를 사용해 현제 userId값을 저장
                    log.info("userId {}",userId);
                }
                return true;//정상적으로 처리된 경우
            }
        }
        return HandlerInterceptor.super.preHandle(request,response,handler);//어노테이션이 적용되지 않은 경우 인터셉터 동작을 수행한다
    }
}