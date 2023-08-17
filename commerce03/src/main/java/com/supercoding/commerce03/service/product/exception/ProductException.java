package com.supercoding.commerce03.service.product.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductException extends RuntimeException {

    private ProductErrorCode errorCode;
    private String errorMessage;

    public ProductException(ProductErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
