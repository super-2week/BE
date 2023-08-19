package com.supercoding.commerce03.service.security;

import com.supercoding.commerce03.repository.user.entity.User;
import com.supercoding.commerce03.repository.user.entity.UserDetail;
import com.supercoding.commerce03.service.user.UserService;
import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        //헤더에 토큰을 담는 속성이 없으면 비로그인으로 판단
        if (authorizationHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }
        //Bearer(대중적 방식) 로 시작하는 정상 토큰인지 유효성 검사
        if (!authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        //헤더에서 추출한 실제 토큰값 받아오기
        String token = authorizationHeader.split(" ")[1];

        //토큰 만료시간 검사
        if (JwtTokenProvider.isExpired(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        //토큰으로 로그인 ID(이메일)을 가져오기
        Long userId = JwtTokenProvider.getLoginId(token);
        //가져온 사용자의 정보로 실제 사용자 정보를 가져오기
        log.info("잡았다 {}",userId);
        User loginUser = userService.getLoginUser(userId);
        log.info("잡혔다 {}",loginUser);
        //사용자 정보를 기반으로 인증 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginUser.getId(), null, List.of(new SimpleGrantedAuthority("USER"))
        );

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        //권한 부여
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);

    }
}
