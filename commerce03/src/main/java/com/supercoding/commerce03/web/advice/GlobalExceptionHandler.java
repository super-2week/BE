package com.supercoding.commerce03.web.advice;

import static com.supercoding.commerce03.service.cart.exception.CartErrorCode.OUT_OF_STOCK;
import static com.supercoding.commerce03.service.cart.exception.CartErrorCode.PRODUCT_ALREADY_EXISTS;

import com.supercoding.commerce03.repository.order.entity.Order;
import com.supercoding.commerce03.service.cart.exception.CartErrorResponse;
import com.supercoding.commerce03.service.cart.exception.CartException;
import com.supercoding.commerce03.service.order.exception.OrderErrorResponse;
import com.supercoding.commerce03.service.order.exception.OrderException;

import com.supercoding.commerce03.service.payment.exception.PaymentErrorResponse;
import com.supercoding.commerce03.service.payment.exception.PaymentException;
import com.supercoding.commerce03.service.product.exception.ProductErrorResponse;
import com.supercoding.commerce03.service.product.exception.ProductException;

import com.supercoding.commerce03.service.review.exception.ReviewErrorResponse;
import com.supercoding.commerce03.service.review.exception.ReviewException;
import com.supercoding.commerce03.service.user.exception.UserErrorResponse;
import com.supercoding.commerce03.service.user.exception.UserException;
import com.supercoding.commerce03.web.advice.exception.ErrorResponse;
import com.supercoding.commerce03.web.advice.exception.type.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidationException(
			MethodArgumentNotValidException e
	){
		ErrorResponse errorResponse = new ErrorResponse(ErrorCode.BAD_REQUEST,
				e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
		return ResponseEntity.badRequest().body(errorResponse);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> handleHttpMessageNotReadableException(
			HttpMessageNotReadableException e
	){
		ErrorResponse errorResponse = new ErrorResponse(ErrorCode.BAD_REQUEST,
				"Request Body가 비어 있습니다");
		return ResponseEntity.badRequest().body(errorResponse);
	}

	@ExceptionHandler(CartException.class)
	public ResponseEntity<?> handleCartException(CartException e){

		return ResponseEntity.status(e.getErrorCode().getHttpStatus())
				.body(CartErrorResponse.builder()
						.errorCode(e.getErrorCode())
						.errorMessage(e.getErrorMessage())
						.build());

	}
	@ExceptionHandler(OrderException.class)
	public ResponseEntity<?> handleOrderException(OrderException e){

		return ResponseEntity.status(e.getErrorCode().getHttpStatus())
				.body(OrderErrorResponse.builder()
						.errorCode(e.getErrorCode())
						.errorMessage(e.getErrorMessage())
						.build());

	}
	@ExceptionHandler(ReviewException.class)
	public ResponseEntity<?> handleReviewException(ReviewException e){

		return ResponseEntity.status(e.getErrorCode().getHttpStatus())
				.body(ReviewErrorResponse.builder()
						.errorCode(e.getErrorCode())
						.errorMessage(e.getErrorMessage())
						.build());

	}
	@ExceptionHandler(UserException.class)
	public ResponseEntity<?> handleUserException(UserException e){

		return ResponseEntity.status(e.getErrorCode().getHttpStatus())
			.body(UserErrorResponse.builder()
				.errorCode(e.getErrorCode())
				.errorMessage(e.getErrorMessage())
				.build());

	}

	@ExceptionHandler(ProductException.class)
	public ResponseEntity<?> handleProductException(ProductException e){

		return ResponseEntity.status(e.getErrorCode().getHttpStatus())
				.body(ProductErrorResponse.builder()
						.errorCode(e.getErrorCode())
						.errorMessage(e.getErrorMessage())
						.build());

	}

	@ExceptionHandler(PaymentException.class)
	public ResponseEntity<?> handlePaymentException(PaymentException e){

		return ResponseEntity.status(e.getErrorCode().getHttpStatus())
				.body(PaymentErrorResponse.builder()
						.errorCode(e.getErrorCode())
						.errorMessage(e.getErrorMessage())
						.build());

	}

}
