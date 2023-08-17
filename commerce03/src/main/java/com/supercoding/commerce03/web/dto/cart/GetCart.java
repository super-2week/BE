package com.supercoding.commerce03.web.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class GetCart {

	@Setter
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class Response {

		private Long cartId;
		private Long productId;
		private String productName;
		private Integer price;
		private Integer quantity;
		private Integer total;
		private String imageUrl;

		public static GetCart.Response from(CartDto cartDto){
			return GetCart.Response.builder()
					.cartId(cartDto.getCartId())
					.productId(cartDto.getProductId())
					.productName(cartDto.getProductName())
					.price(cartDto.getPrice())
					.quantity(cartDto.getQuantity())
					.total(cartDto.getTotal())
					.imageUrl(cartDto.getImageUrl())
					.build();
		}
	}

}
