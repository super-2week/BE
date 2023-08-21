package com.supercoding.commerce03.service.user;

import com.supercoding.commerce03.repository.user.UserDetailRepository;
import com.supercoding.commerce03.repository.user.UserRepository;
import com.supercoding.commerce03.repository.user.entity.User;
import com.supercoding.commerce03.repository.user.entity.UserDetail;
import com.supercoding.commerce03.service.security.JwtTokenProvider;
import com.supercoding.commerce03.service.user.exception.UserErrorCode;
import com.supercoding.commerce03.service.user.exception.UserException;
import com.supercoding.commerce03.web.dto.user.KakaoLogin;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KaKaoLoginService {

    private final UserRepository userRepository;

    private final UserDetailRepository userDetailRepository;

    public ResponseEntity<String> emailExists(KakaoLogin kakaoLogin) {
        //이메일과 회원 이름 이 들어온다 (이메일을 갖고올 때 isDelete t인지 f인지)
        Optional<UserDetail> optionalUserDetail = userDetailRepository.findByEmail(kakaoLogin.getEmail());

        //만약 이메일이 현재 존재하지 않는 경우에는
        if (optionalUserDetail.isEmpty()){
            User user = userRepository.save(User.builder()
                    .createdAt(LocalDateTime.now())
                    .userName(kakaoLogin.getUserName())
                    .isDeleted(false)
                .build());

            UserDetail userDetail = userDetailRepository.save(UserDetail.builder()
                .email(kakaoLogin.getEmail())
                .build());
            //토큰 생성
           String token = makeLoginResponse(user,userDetail);
            //해당 메세지를 보낸다
            HttpHeaders header = new HttpHeaders();
            header.set("Authorization", "Bearer " + token);

            return  new ResponseEntity<>("신규회원 입니다 회원 수정으로 이동해주세요",header,HttpStatus.ACCEPTED);
        }
        else {
            //토큰을 생성한다
            String token = makeLoginResponse(optionalUserDetail.get().getUser(), optionalUserDetail.get());

            //해당 메세지를 보낸다
            HttpHeaders header = new HttpHeaders();
            header.set("Authorization", "Bearer " + token);

            return new ResponseEntity<>("기존 회원입니다 로그인 완료!",header,HttpStatus.OK);

        }
    }


    public String makeLoginResponse(User user, UserDetail userDetail) {
        return JwtTokenProvider.createToken(user, userDetail);
    }
}
