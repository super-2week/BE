package com.supercoding.commerce03.web.dto.cart;

import java.text.DecimalFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class ModifyCart {

	@Setter
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class Request {

		private Long cartId;
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
		private String total;

		public static Response from(CartDto cartDto){
			return Response.builder()
					.cartId(cartDto.getCartId())
					.productId(cartDto.getProductId())
					.productName(cartDto.getProductName())
					.quantity(cartDto.getQuantity())
					.total(formatter.format(cartDto.getTotal()))
					.build();
		}

		private static DecimalFormat formatter = new DecimalFormat("###,###");
	}

}
