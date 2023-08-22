package com.supercoding.commerce03.web.controller.user;

import com.supercoding.commerce03.service.security.Auth;
import com.supercoding.commerce03.service.security.AuthHolder;
import com.supercoding.commerce03.service.user.UserService;
import com.supercoding.commerce03.web.dto.user.Login;
import com.supercoding.commerce03.web.dto.user.SignUp;
import com.supercoding.commerce03.web.dto.user.UpdateProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v1/api/users")
@RestController
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Object> signUp(@RequestBody SignUp signUp) {
        return ResponseEntity.ok(userService.signUp(signUp));
    }
    @PostMapping("/signin")
    public ResponseEntity<Login.Response> login(@RequestBody Login.Request loginRequest) {

        return ResponseEntity.ok(userService.login(loginRequest));
    }

    @Auth
    @DeleteMapping
    public ResponseEntity<Object> delete(){
        Long userId = AuthHolder.getUserId();
        log.info("잡았다 {}",AuthHolder.getUserId());
        return ResponseEntity.ok(userService.deleteUser(userId));
    }

    @Auth
    @GetMapping
    public ResponseEntity<Object> getMyProfile(){
        Long userId = AuthHolder.getUserId();
        return ResponseEntity.ok(userService.getProfile(userId));
    }

    @Auth
    @PatchMapping
    public ResponseEntity<Object>updateProfile(@RequestBody UpdateProfile updateProfile){
        Long userId = AuthHolder.getUserId();
        return ResponseEntity.ok(userService.updateUser(userId,updateProfile));
    }
}

