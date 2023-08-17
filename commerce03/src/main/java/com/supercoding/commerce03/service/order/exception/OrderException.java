package com.supercoding.commerce03.service.order.exception;


public class OrderException extends RuntimeException{
    private OrderErrorCode errorCode;
    private String errorMessage;

    public OrderException(OrderErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
