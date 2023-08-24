package com.supercoding.commerce03.service.cart;

import com.supercoding.commerce03.client.RedisClient;
import com.supercoding.commerce03.repository.product.ProductRepository;
import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.repository.redis.Basket;
import com.supercoding.commerce03.repository.redis.Basket.Item;
import com.supercoding.commerce03.repository.user.UserRepository;
import com.supercoding.commerce03.repository.user.entity.User;
import com.supercoding.commerce03.service.cart.exception.CartErrorCode;
import com.supercoding.commerce03.service.cart.exception.CartException;
import com.supercoding.commerce03.web.dto.cart.AddCart;
import com.supercoding.commerce03.web.dto.cart.ModifyCart.Request;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@Slf4j
@RequiredArgsConstructor
public class BasketService {

	private final RedisClient redisClient;
	private final RedisTemplate<String, Object> redisTemplate;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;

	public Basket getBasket(Long userId){
		Basket basket = redisClient.get(userId, Basket.class);
		return basket != null ? basket : new Basket();
	}

	public Basket addBasket(Long userId, AddCart.Request request){

		Long inputProductId = request.getProductId();
		Integer inputQuantity = request.getQuantity();

		validateUser(userId);
		Product validatedProduct = validateProduct(inputProductId);
		validateQuantity(inputQuantity, validatedProduct);

		Basket basket = redisClient.get(userId, Basket.class);
		if (basket == null) {
			basket = new Basket();
			basket.setUserId(userId);
		}

		Optional<Item> optionalItem = basket.getProducts().stream()
				.filter(p -> p.getProductId().equals(inputProductId))
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

	public void insertItemList(Long userId, List<Item> itemList){

		final String key = userId.toString();

		RedisSerializer<String> keySerializer = redisTemplate.getStringSerializer();
		RedisSerializer valueSerializer = redisTemplate.getValueSerializer();

		redisTemplate.executePipelined((RedisCallback<Object>) RedisConnection -> {
			itemList.forEach(item -> {
				String Key = item.getProductId().toString();
				String Value = valueSerializer.serialize(item).toString();

				RedisConnection.hSet(keySerializer.serialize(key),
						keySerializer.serialize(Key),
						valueSerializer.serialize(Value)
				);
			});
			return null;
		});
	}

	public void restoreBasketListOnOrderRollback(Long userId, List<Item> itemList) {
		TransactionSynchronizationManager.registerSynchronization(
				new TransactionSynchronization() {
					@Override
					public void afterCompletion(int status) {
						if (status == STATUS_ROLLED_BACK) {
							insertItemList(userId, itemList);
						}
					}
				});
	}

	private User validateUser(Long userId){
		return userRepository.findById(userId)
				.orElseThrow(() -> new CartException(CartErrorCode.USER_NOT_FOUND));
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

	private void validateQuantity(Integer inputQuantity, Product product){
		if (inputQuantity <= 0 || inputQuantity > product.getStock()) {
			throw new CartException(CartErrorCode.INVALID_QUANTITY);
		}
	}

	private void run(Long userId, List<Item> itemList) {
		restoreBasketListOnOrderRollback(userId, itemList);
	}
}
