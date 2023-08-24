package com.supercoding.commerce03.repository.redis;

import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.web.dto.cart.AddCart;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Data
@NoArgsConstructor
@RedisHash("basket")
public class Basket {

	@Id
	private Long userId;
	private List<Item> products = new ArrayList<>();

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Item {

		private Long productId;
		private String productName;
		private Integer price;
		private Integer quantity;
		private String imageUrl;
		private Integer total;

		public static Item from(AddCart.Request request, Product product){
			return Item.builder()
					.productId(product.getId())
					.productName(product.getProductName())
					.price(product.getPrice())
					.quantity(request.getQuantity())
					.imageUrl(product.getImageUrl())
					.total(product.getPrice() * request.getQuantity())
					.build();
		}
	}
}
