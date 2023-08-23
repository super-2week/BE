package com.supercoding.commerce03.service.user;

import com.supercoding.commerce03.repository.user.UserDetailRepository;
import com.supercoding.commerce03.repository.user.UserRepository;
import com.supercoding.commerce03.repository.user.entity.User;
import com.supercoding.commerce03.repository.user.entity.UserDetail;
import com.supercoding.commerce03.service.S3.S3Service;
import com.supercoding.commerce03.service.payment.PaymentService;
import com.supercoding.commerce03.service.security.JwtTokenProvider;
import com.supercoding.commerce03.service.user.exception.UserErrorCode;
import com.supercoding.commerce03.service.user.exception.UserException;
import com.supercoding.commerce03.web.dto.user.Login;
import com.supercoding.commerce03.web.dto.user.ProfileResponse;
import com.supercoding.commerce03.web.dto.user.SignUp;
import com.supercoding.commerce03.web.dto.user.UpdateProfile;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PaymentService paymentService;

    private final S3Service s3Service;


    @Transactional
    public String signUp(SignUp signUp) {
        //비지니스 로직(들어오는것들을 검증하는 부분)

        //검증된 정보들
        User user = userRepository.save(User.toEntity(signUp));
        paymentService.createPayment(user);
        userDetailRepository.save(
            UserDetail.toEntity(user, signUp, passwordEncoder.encode(signUp.getPassword())));

        return "회원가입이 성공적으로 완료되었습니다";
    }

    public String emailDuplicate(String email) {
       boolean checkEmail = userDetailRepository.existsByEmail(email);
        if (checkEmail) {
            throw new UserException(UserErrorCode.EMAIL_DUPLICATION);
        }
            return "사용가능한 이메일입니다";
    }

    @Transactional
    public Login.Response login(Login.Request loginRequest) {
        Optional<UserDetail> optionalUserDetail = userDetailRepository.findByEmail(
            loginRequest.getEmail());

        if (optionalUserDetail.isEmpty()) {
            throw new UserException(UserErrorCode.INVALID_SIGNUP_FILED);
        }
        UserDetail loginUser = optionalUserDetail.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), loginUser.getPassword())) {
            throw new UserException(UserErrorCode.INVALID_LOGIN_INPUT);
        }
        String token = makeLoginResponse(loginUser.getUser(), loginUser);

        return Login.Response.builder()
            .userId(loginUser.getUser().getId())
            .email(loginUser.getEmail())
            .Token(token)
            .build();
    }
    public User getLoginUser(Long userId) {
        if (userId == null) {
            throw new UserException(UserErrorCode.INVALID_LOGIN_INPUT);
        }
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserException(UserErrorCode.INVALID_LOGIN_INPUT);
        }
        return optionalUser.get();
    }

    @Transactional
    public String deleteUser(Long userId) {
        if (userId == null) {
            throw new UserException(UserErrorCode.INVALID_LOGIN_INPUT);
        }
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.NOT_AUTHORIZED));

        user.setIsDeleted(true);
        return user.getUserName();
    }

    public ProfileResponse getProfile(Long userId) {
        UserDetail userDetail = userDetailRepository.findByUserId(userId);
        if (userId == null) {
            throw new UserException(UserErrorCode.USER_NOT_FOUND);
        }
        return ProfileResponse.fromEntity(userDetail);
    }

    @Transactional
    public String updateUser(Long userId, UpdateProfile updateProfile, MultipartFile multipartFile) {

        User user = userRepository.findById(userId)
            .orElseThrow(()-> new UserException(UserErrorCode.USER_NOT_FOUND));

        //기존에 있던 파일 삭제
        if(user.getImageUrl() != null) {
            s3Service.deleteFile(user.getImageUrl());
        }
        //사진 업로드
        String image = s3Service.uploadFile(multipartFile);

        user.setImageUrl(image);

        UserDetail userDetail = userDetailRepository.findByUserId(userId);//회원의 ID로 접근 권한 처리

        UserDetail.update(userDetail,updateProfile,passwordEncoder.encode(updateProfile.getPassword()));

        User.update(user,updateProfile);
        return "회원수정이 성공적으로 완료되었습니다";
    }
    public String makeLoginResponse(User user, UserDetail loginUser) {
        return JwtTokenProvider.createToken(user, loginUser);
    }
}
