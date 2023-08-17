package com.supercoding.commerce03.service.user;

import com.supercoding.commerce03.repository.user.UserDetailRepository;
import com.supercoding.commerce03.repository.user.UserRepository;
import com.supercoding.commerce03.repository.user.entity.User;
import com.supercoding.commerce03.repository.user.entity.UserDetail;
import com.supercoding.commerce03.service.security.JwtTokenProvider;
import com.supercoding.commerce03.service.user.exception.UserErrorCode;
import com.supercoding.commerce03.service.user.exception.UserException;
import com.supercoding.commerce03.web.dto.user.Login;
import com.supercoding.commerce03.web.dto.user.SignUp;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    public String signUp(SignUp signUp) {
   //비지니스 로직(들어오는것들을 검증하는 부분)
        emailDuplicate(signUp.getEmail());
        validatedPhoneNumber(signUp.getPhoneNumber());
        validatedPassword(signUp.getPassword());

   //검증된 정보들

        User user = userRepository.save(User.toEntity(signUp));
        log.info("잡았다");
        userDetailRepository.save(UserDetail.toEntity(user,signUp,passwordEncoder.encode(signUp.getPassword())));
        log.info("잡았다");

        return "회원가입이 성공적으로 완료되었습니다";
    }

    public void emailDuplicate(String email) {
        boolean checkEmail = userDetailRepository.existsByEmail(email);
        if(checkEmail){
            throw new UserException(UserErrorCode.EMAIL_DUPLICATION);
        }
    }
    public void validatedPhoneNumber(String phoneNumber) {
        if(phoneNumber.length() != 11){
            throw new UserException(UserErrorCode.INVALID_PHONE_NUMBER);
        }
    }
    public void validatedPassword(String password){
        if(password.length() < 8){
            throw new UserException(UserErrorCode.INVALID_PASSWORD);
        }
        if(!password.matches("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$")){
            throw new UserException(UserErrorCode.INVALID_PHONE_NUMBER_PATTERN);
        }
    }

    public Login.Response login(Login.Request loginRequest) {
        Optional<UserDetail>optionalUserDetail = userDetailRepository.findByEmail(loginRequest.getEmail());

        if (optionalUserDetail.isEmpty()){
            throw new UserException(UserErrorCode.INVALID_SIGNUP_FILED);
        }
        UserDetail loginUser = optionalUserDetail.get();
        log.info("여기다! {}",loginRequest.getPassword());
        log.info("여기다! {}",loginUser.getPassword());
        if(!passwordEncoder.matches(loginRequest.getPassword(),loginUser.getPassword())){
            throw new UserException(UserErrorCode.INVALID_LOGIN_INPUT);
        }
        String token = makeLoginResponse(loginUser.getUser(), loginUser);

        return Login.Response.builder()
            .userId(loginUser.getUser().getId())
            .email(loginUser.getEmail())
            .Token(token)
            .build();
    }

    public String makeLoginResponse(User user,UserDetail loginUser){
        return JwtTokenProvider.createToken(user,loginUser);
    }
}
