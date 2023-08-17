package com.supercoding.commerce03.web.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class RemoveCart {

	@Setter
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class Response {

		private Long productId;
		private String productName;

		public static Response from(CartDto cartDto){
			return Response.builder()
					.productId(cartDto.getProductId())
					.productName(cartDto.getProductName())
					.build();
		}
	}

}
