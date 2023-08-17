package com.supercoding.commerce03.service.order.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum OrderErrorCode {
    NEVER_ORDERED_BEFORE("해당 유저는 주문을 한 적이 없습니다.",HttpStatus.OK),

    USER_NOT_FOUND("존재하지 않는 유저 입니다.", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND("주문한 상품이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    ORDER_NOT_FOUND("해당 주문이 존재하지 않습니다.",HttpStatus.NOT_FOUND),

    OUT_OF_STOCK("주문한 상품의 재고가 부족합니다.",HttpStatus.CONFLICT),
    LACK_OF_POINT("포인트가 주문 금액에 비해 부족합니다.",HttpStatus.CONFLICT);


    private final String description;
    private final HttpStatus httpStatus;

}
