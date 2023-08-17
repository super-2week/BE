package com.supercoding.commerce03.web.dto.cart;

import com.supercoding.commerce03.repository.cart.entity.Cart;
import com.supercoding.commerce03.repository.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {

	private Long userId;

	private Long cartId;

	private Long productId;
	private String productName;
	private Integer price;
	private String imageUrl;
	private Integer quantity;
	private Integer total;

	public static CartDto fromEntity(Cart cart){
		Product product = cart.getProduct();
		return CartDto.builder()
				.userId(cart.getUser().getId())
				.cartId(cart.getId())
				.productId(product.getId())
				.productName(product.getProductName())
				.price(product.getPrice())
				.imageUrl(product.getImageUrl())
				.quantity(cart.getQuantity())
				.total(product.getPrice() * cart.getQuantity())
				.build();
	}
}
