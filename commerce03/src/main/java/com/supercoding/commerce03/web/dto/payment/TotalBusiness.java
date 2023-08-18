package com.supercoding.commerce03.web.dto.payment;

import com.supercoding.commerce03.repository.payment.entity.BusinessByType;
import com.supercoding.commerce03.repository.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

public class TotalBusiness {

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {

        private User userName;
        private BusinessByType businessType;
        private Integer coin;
        private LocalDateTime createdAt;
        private Integer totalCoin;


    }
}
