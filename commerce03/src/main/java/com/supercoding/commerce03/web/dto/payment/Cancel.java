package com.supercoding.commerce03.web.dto.payment;

import com.supercoding.commerce03.repository.payment.entity.BusinessByType;
import com.supercoding.commerce03.repository.payment.entity.Payment;
import lombok.*;

import java.time.LocalDateTime;

public class Cancel {

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {

        private Integer coin;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {

        private BusinessByType businessType;
        private LocalDateTime createdAt;
        private Integer coin;

        public static Cancel.Response from(Payment payment) {
            return Cancel.Response.builder()
                    .businessType(payment.getBusinessType())
                    .createdAt(payment.getCreatedAt())
                    .coin(payment.getCoin())
                    .build();
        }
    }
}

