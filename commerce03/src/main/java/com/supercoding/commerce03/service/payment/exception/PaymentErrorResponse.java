package com.supercoding.commerce03.service.payment.exception;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PaymentErrorResponse {
    private PaymentErrorCode errorCode;
    private String errorMessage;
}
