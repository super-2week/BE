package com.supercoding.commerce03.web.dto.payment;

import com.supercoding.commerce03.repository.payment.entity.Payment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class TotalCoin {

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {

        private Integer totalPayCoin;
        private List<TotalCoin.Response.PaymentDetail> paymentDetails;

        public static TotalCoin.Response from(Payment payment, List<TotalCoin.Response.PaymentDetail> paymentDetails) {
            return TotalCoin.Response.builder()
                    .totalPayCoin(payment.getTotalCoin())
                    .paymentDetails(paymentDetails)
                    .build();
        }

        @Setter
        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class PaymentDetail{
            private String businessType;
            private LocalDateTime createdAt;
            private Integer coin;
            private Integer totalCoin;
        }
    }
}


