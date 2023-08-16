package com.supercoding.commerce03.service.security;

import com.supercoding.commerce03.repository.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private static final String SECRET_KEY = "dSa2Hased =";

    public static String createToken(User user) {
        // 토큰의 만료 날짜 설정
        Date expiryDate = Date.from(
            Instant.now()
                .plus(1, ChronoUnit.HOURS)
        );
        // JWT 클레임 설정
        Claims claims = Jwts.claims();
        claims.put("userId", user.getId());//사용자 정보 담기
        claims.put("email", user.getUserDetail().getEmail());//아이디 추가 보통 아이디,식별값 정도면 충분함

        // JWT 토큰 생성 및 반환(사용자 ID, 이름)
        return Jwts.builder()
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)  // 토큰 서명 설정
            .setClaims(claims)  // JWT 클레임 설정한거 추가
            .setExpiration(expiryDate)  // 토큰 만료 날짜 설정 추가
            .compact();  // 토큰 생성 후 문자열 형태로 반환
    }
    public static boolean isExpired(String token) {
        try {
            Date expiredDate = extractClaims(token).getExpiration();
            // Token의 만료 날짜가 지금보다 이전인지 check 이전이면 만료
            return expiredDate.before(new Date());
        } catch (RuntimeException e) {
            throw new RuntimeException("로그인을 다시 해주세요");
        }
    }
    public static Long getLoginId(String token){
        return Long.valueOf(extractClaims(token).get("email").toString());
    }

    private static Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }
}