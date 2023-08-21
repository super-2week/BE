package com.supercoding.commerce03.web.dto.payment;

import com.supercoding.commerce03.repository.payment.entity.BusinessByType;
import com.supercoding.commerce03.repository.payment.entity.Payment;
import com.supercoding.commerce03.repository.payment.entity.PaymentDetail;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

//        public static List<Charge.Response> from(List<Payment> list){
//            return list.stream().map(Charge.Response::from).collect(Collectors.toList());
//        }

        public static Charge.Response from(PaymentDetail paymentDetail) {
            return Response.builder()
                    .businessType(paymentDetail.getBusinessType())
                    .createdAt(paymentDetail.getCreatedAt())
                    .coin(paymentDetail.getPayCoin())
                    .totalCoin(paymentDetail.getTotalPayCoin())
                    .build();
        }
    }
}


