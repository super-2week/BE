package com.supercoding.commerce03.service.product.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductErrorResponse {

    private ProductErrorCode errorCode;
    private String errorMessage;
}
