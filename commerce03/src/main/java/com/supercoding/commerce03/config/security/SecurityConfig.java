package com.supercoding.commerce03.config.security;

import com.supercoding.commerce03.service.security.TokenFilter;
import com.supercoding.commerce03.service.user.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailService userDetailService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin()
            .and()
            .formLogin().disable()
            .csrf().disable()//세션과 진행 토큰 안정화
            .cors()
            .and()
            .httpBasic().disable()
            .rememberMe().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(new TokenFilter(userDetailService), UsernamePasswordAuthenticationFilter.class)//인증에대한 검사
            .authorizeRequests()
            .antMatchers("/**").permitAll();//권한이 있어야 호출가능한지 검사;
        return http.build();
    }
}