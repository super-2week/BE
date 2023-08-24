package com.supercoding.commerce03.service.cart;

import com.supercoding.commerce03.client.RedisClient;
import com.supercoding.commerce03.repository.product.ProductRepository;
import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.repository.redis.Basket;
import com.supercoding.commerce03.repository.redis.Basket.Item;
import com.supercoding.commerce03.service.cart.exception.CartErrorCode;
import com.supercoding.commerce03.service.cart.exception.CartException;
import com.supercoding.commerce03.web.dto.cart.AddCart;
import com.supercoding.commerce03.web.dto.cart.ModifyCart.Request;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BasketService {

	private final RedisClient redisClient;
	private final ProductRepository productRepository;

	public Basket getBasket(Long userId){
		Basket basket = redisClient.get(userId, Basket.class);
		return basket != null ? basket : new Basket();
	}

	public Basket addBasket(Long userId, AddCart.Request request){

		Product validatedProduct = validateProduct(request.getProductId());

		Basket basket = redisClient.get(userId, Basket.class);
		if (basket == null) {
			basket = new Basket();
			basket.setUserId(userId);
		}

		Optional<Item> optionalItem = basket.getProducts().stream()
				.filter(p -> p.getProductId().equals(request.getProductId()))
				.findFirst();

		if (optionalItem.isPresent()) {
			throw new CartException(CartErrorCode.PRODUCT_ALREADY_EXISTS);
		} else {
			Item item = Item.from(request, validatedProduct);
			basket.getProducts().add(item);
		}

		redisClient.put(userId, basket);
		return basket;
	}

	public Basket modifyBasket(Long userId, Request request){

		Basket basket = redisClient.get(userId, Basket.class);

		Optional<Item> optionalItem = basket.getProducts().stream()
				.filter(p -> p.getProductId().equals(request.getProductId()))
				.findFirst();

		if (optionalItem.isPresent()) {
			Item item = optionalItem.get();
			item.setQuantity(request.getQuantity());
			item.setTotal(item.getPrice() * request.getQuantity());
		}

		redisClient.put(userId, basket);
		return basket;
	}

	public Basket removeBasket(Long userId, Long productId){

		Basket basket = redisClient.get(userId, Basket.class);
		Optional<Item> optionalItem = basket.getProducts().stream()
				.filter(p -> p.getProductId().equals(productId))
				.findFirst();

		optionalItem.ifPresent(item -> basket.getProducts().remove(item));

		redisClient.put(userId, basket);
		return basket;
	}

	private Product validateProduct(Long productId){
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new CartException(CartErrorCode.THIS_PRODUCT_DOES_NOT_EXIST));

		Integer stock = product.getStock();
		if (stock <= 0) {
			throw new CartException(CartErrorCode.OUT_OF_STOCK);
		}

		return product;
	}
}
