package com.supercoding.commerce03.service.product.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ProductErrorCode {
    //status(HttpStatus.badRequest) 400
    INVALID_QUANTITY("수량을 확인해주세요.", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("존재하지 않는 유저 입니다.", HttpStatus.NOT_FOUND),
    THIS_PRODUCT_DOES_NOT_EXIST("존재하지 않는 상품 입니다.", HttpStatus.NOT_FOUND),

    //status(HttpStatus.FORBIDDEN)403
    INVALID_USER("로그인이 필요합니다.", HttpStatus.FORBIDDEN),

    //status(HttpStatus.CONFLICT) 409
    OUT_OF_STOCK("상품이 품절되었습니다.", HttpStatus.CONFLICT),
    NOT_FOUND_IN_WISHLIST("관심상품 리스트에 없는 상품입니다.", HttpStatus.CONFLICT),
    ALREADY_EXISTS_IN_WISHLIST("이미 찜한 상품입니다.", HttpStatus.CONFLICT);

    private final String description;
    private final HttpStatus httpStatus;

}
