package com.supercoding.commerce03.web.dto.user;

import com.supercoding.commerce03.repository.user.entity.UserDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class ProfileResponse {

        private String userName;
        private String imageUrl;
        private String email;
        private String phoneNumber;
        private String address;
        private String detailAddress;

    public static ProfileResponse fromEntity(UserDetail userDetail){
        return ProfileResponse.builder()
            .userName(userDetail.getUser().getUserName())
            .imageUrl(userDetail.getUser().getImageUrl())
            .email(userDetail.getEmail())
            .phoneNumber(userDetail.getPhoneNumber())
            .address(userDetail.getAddress())
            .detailAddress(userDetail.getDetailAddress())
            .build();
     }
}



