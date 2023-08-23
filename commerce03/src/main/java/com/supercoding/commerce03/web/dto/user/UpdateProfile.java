package com.supercoding.commerce03.web.dto.user;

import com.supercoding.commerce03.repository.user.entity.UserDetail;
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
public class UpdateProfile {
        private String userName;
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$", message = "비밀번호는 영어와 숫자 , 특수문자를 포함해서 8자리 이상으로 입력해주세요.")
        private String password;
        private String address;
        private String detailAddress;

}

