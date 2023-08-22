package com.supercoding.commerce03.service.security;

import com.supercoding.commerce03.repository.user.entity.User;
import com.supercoding.commerce03.repository.user.entity.UserDetail;
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
    private static final String SECRET_KEY = "c3VwZXJjb2Rpbmc=";

    public static String createToken(User user, UserDetail userDetail) {
        // 토큰의 만료 날짜 설정
        Date expiryDate = Date.from(
            Instant.now()
                .plus(1, ChronoUnit.DAYS)
        );
        // JWT 클레임 설정
        Claims claims = Jwts.claims();
        claims.put("userId", user.getId());//사용자 정보 담기
        claims.put("email", userDetail.getEmail());//아이디 추가 보통 아이디,식별값 정도면 충분함

        // JWT 토큰 생성 및 반환(사용자 ID, 이름)
        return Jwts.builder()
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)  // 토큰 서명 설정
            .setClaims(claims)  // JWT 클레임 설정한거 추가
            .setExpiration(expiryDate)  // 토큰 만료 날짜 설정 추가
            .compact();  // 토큰 생성 후 문자열 형태로 반환
    }

//    public static String getLoginEmail(String token){
//        return extractClaims(token).get("Email").toString();
//    }

    public static Long getLoginId(String token){
        return Long.valueOf(extractClaims(token).get("userId").toString());//userId가 Long타입이 아닌 다른타입으로 들어옴 toString으로 바꾼 뒤 Long으로 형변환
    }
    public static boolean isExpired(String token) {
        Date expiredDate = extractClaims(token).getExpiration();
        // Token의 만료 날짜가 지금보다 이전인지 check 이전이면 만료
        return expiredDate.before(new Date());
    }
    //SecretKey를 사용해 Token Parsing
    private static Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }
}