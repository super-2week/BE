package com.supercoding.commerce03.web.controller.user;

import com.supercoding.commerce03.service.user.KaKaoLoginService;
import com.supercoding.commerce03.web.dto.user.KakaoLogin;
import com.supercoding.commerce03.web.dto.user.SignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/kakao")
public class KakaoLoginController {
    private final KaKaoLoginService kaKaoLoginService;
    @PostMapping
    public ResponseEntity<String> kakaoLogin(@RequestBody KakaoLogin kakaoLogin) {

    return kaKaoLoginService.emailExists(kakaoLogin);
    }
}
