package com.supercoding.commerce03.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // 허용할 Origin, 필요에 따라 변경
        corsConfiguration.addAllowedHeader("*"); // 허용할 Header, 필요에 따라 변경
        corsConfiguration.addAllowedMethod("*"); // 허용할 HTTP Method, 필요에 따라 변경
        corsConfiguration.addExposedHeader("Authorization"); // 노출할 응답 헤더

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }
}