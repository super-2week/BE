package com.supercoding.commerce03.web.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class AddCart {

	@Setter
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class Request {

		private Long productId;
		private Integer quantity;
	}

	@Setter
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class Response {

		private Long cartId;
		private Long productId;
		private String productName;
		private Integer quantity;

		public static Response from(CartDto cartDto) {
			return Response.builder()
					.cartId(cartDto.getCartId())
					.productId(cartDto.getProductId())
					.productName(cartDto.getProductName())
					.quantity(cartDto.getQuantity())
					.build();
		}
	}

}
