package com.supercoding.commerce03.web.advice;

import com.supercoding.commerce03.web.advice.exception.ErrorResponse;
import com.supercoding.commerce03.web.advice.exception.type.ErrorCode;
import lombok.extern.slf4j.Slf4j;
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

}
