package com.supercoding.commerce03.service.review.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReviewErrorCode {

	//status(HttpStatus.FORBIDDEN) 403
	REVIEW_PERMISSION_DENIED("이 상품에 대한 리뷰를 작성할 권한이 없습니다.", HttpStatus.FORBIDDEN),
	NO_PERMISSION_TO_UPDATE("이 상품에 대한 리뷰를 수정할 권한이 없습니다.", HttpStatus.FORBIDDEN),
	NO_PERMISSION_TO_DELETE("이 상품에 대한 리뷰를 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN),

	//status(HttpStatus.NOT_FOUND) 404
	USER_NOT_FOUND("존재하지 않는 유저 입니다.", HttpStatus.NOT_FOUND),
	THIS_PRODUCT_DOES_NOT_EXIST("존재하지 않는 상품 입니다.", HttpStatus.NOT_FOUND),
	REVIEW_DOES_NOT_EXIST("리뷰가 존재하지 않습니다.", HttpStatus.NOT_FOUND),

	//status(HttpStatus.CONFLICT) 409
	REVIEW_ALREADY_EXISTS("리뷰가 이미 존재합니다.", HttpStatus.CONFLICT);


	private final String description;
	private final HttpStatus httpStatus;
}
