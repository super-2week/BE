package com.supercoding.commerce03.service.order.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderErrorCode {
    USER_NOT_FOUND("존재하지 않는 유저 입니다."),
    PRODUCT_NOT_FOUND("주문한 상품이 존재하지 않습니다."),
    OUT_OF_STOCK("주문한 상품의 재고가 부족합니다."),
    LACK_OF_POINT("포인트가 주문 금액보다 부족합니다.");

    private final String description;


}
