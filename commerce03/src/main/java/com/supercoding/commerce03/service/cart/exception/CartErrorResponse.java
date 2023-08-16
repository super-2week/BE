package com.supercoding.commerce03.service.cart.exception;

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
public class CartErrorResponse {

	private CartErrorCode errorCode;
	private String errorMessage;
}
