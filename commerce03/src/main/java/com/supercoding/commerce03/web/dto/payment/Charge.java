package com.supercoding.commerce03.web.dto.payment;

import com.supercoding.commerce03.repository.payment.entity.BusinessByType;
import com.supercoding.commerce03.repository.payment.entity.Payment;
import com.supercoding.commerce03.repository.payment.entity.PaymentDetail;
import lombok.*;

import java.text.DecimalFormat;
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

        private String businessType;
        private LocalDateTime createdAt;
        private String coin;
        private String totalCoin;

        public static Charge.Response from(Payment payment) {
            return Response.builder()
                    .businessType(payment.getBusinessType().getKey())
                    .createdAt(payment.getCreatedAt())
                    .coin(formatter.format(payment.getCoin()))
                    .totalCoin(formatter.format(payment.getTotalCoin()))
                    .build();
        }

        public static Charge.Response from(PaymentDetail paymentDetail) {
            return Response.builder()
                    .businessType(paymentDetail.getBusinessType().getKey())
                    .createdAt(paymentDetail.getCreatedAt())
                    .coin(formatter.format(paymentDetail.getPayCoin()))
                    .totalCoin(formatter.format(paymentDetail.getTotalPayCoin()))
                    .build();
        }

        private static DecimalFormat formatter = new DecimalFormat("###,###");
    }
}


