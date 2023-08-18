package com.supercoding.commerce03.service.review.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewException extends RuntimeException {

	private ReviewErrorCode errorCode;
	private String errorMessage;

	public ReviewException(ReviewErrorCode errorCode) {
		this.errorCode = errorCode;
		this.errorMessage = errorCode.getDescription();
	}
}
