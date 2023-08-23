package com.supercoding.commerce03.web.dto.user;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUp {
    private String userName;
    @NotBlank(message = "비밀번호를 입력하여 주십시오.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$", message = "비밀번호는 영어와 숫자 , 특수문자를 포함해서 8자리 이상으로 입력해주세요.")
    private String password;
    private String address;
    private String detailAddress;
    @Pattern(regexp = "^[0-9]{11}$", message = "11자리 숫자를 입력해주세요.")
    private String phoneNumber;

    @Email
    @NotBlank(message = "이메일을 입력하여 주십시오.")
    private String email;

}
