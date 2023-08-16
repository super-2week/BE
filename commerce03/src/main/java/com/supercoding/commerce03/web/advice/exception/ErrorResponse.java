package com.supercoding.commerce03.web.advice.exception;

import com.supercoding.commerce03.web.advice.exception.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {

	private ErrorCode errorCode;
	private String errorMessage;
}
