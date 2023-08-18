package com.supercoding.commerce03.web.dto.payment;

import com.supercoding.commerce03.repository.payment.entity.BusinessByType;
import com.supercoding.commerce03.repository.payment.entity.Payment;
import lombok.*;

import java.time.LocalDateTime;

public class Charge {

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
        private Integer totalCoin;

        public static Charge.Response from(Payment payment) {
            return Response.builder()
                    .businessType(payment.getBusinessType())
                    .createdAt(payment.getCreatedAt())
                    .coin(payment.getCoin())
                    .totalCoin(payment.getTotalCoin())
                    .build();
        }
    }
}


