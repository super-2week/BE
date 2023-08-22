package com.supercoding.commerce03.service.cart;

import com.supercoding.commerce03.repository.cart.CartRepository;
import com.supercoding.commerce03.repository.cart.entity.Cart;
import com.supercoding.commerce03.repository.product.ProductRepository;
import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.repository.user.UserRepository;
import com.supercoding.commerce03.repository.user.entity.User;
import com.supercoding.commerce03.service.cart.exception.CartErrorCode;
import com.supercoding.commerce03.service.cart.exception.CartException;
import com.supercoding.commerce03.web.dto.cart.AddCart;
import com.supercoding.commerce03.web.dto.cart.CartDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CartService {

	private final ProductRepository productRepository;
	private final CartRepository cartRepository;
	private final UserRepository userRepository;

	@Transactional
	public CartDto addToCart(AddCart.Request request, Long userId){

		Long inputProductId = request.getProductId();
		Integer inputQuantity = request.getQuantity();

		User validatedUser = validateUser(userId);
		Product validatedProduct = validateProduct(inputProductId);
		validateQuantity(inputQuantity, validatedProduct);

		if (existsInCart(userId, inputProductId)) {
			throw new CartException(CartErrorCode.PRODUCT_ALREADY_EXISTS);
		}

		return CartDto.fromEntity(
				cartRepository.save(
						Cart.builder()
								.user(validatedUser)
								.product(validatedProduct)
								.quantity(inputQuantity)
								.isDeleted(false)
								.build()
				)
		);
	}

	@Transactional
	public CartDto removeFromCart(Long cartId, Long userId){

		validateUser(userId);
		Cart validatedCart = validateCart(cartId, userId);

		validatedCart.setIsDeleted(true);

		return CartDto.fromEntity(validatedCart);
	}

	public Page<CartDto> getCart(Long userId, Long cursor, Integer pageSize){

		validateUser(userId);
		Page<Cart> carts = cartRepository.findAllByUserIdAndIsDeletedWithCursor(
				userId, false, cursor, PageRequest.of(0, pageSize));
		return carts.map(CartDto::fromEntity);
	}

	private User validateUser(Long userId){
		return userRepository.findById(userId)
				.orElseThrow(() -> new CartException(CartErrorCode.USER_NOT_FOUND));
	}

	private void validateQuantity(Integer inputQuantity, Product product){
		if (inputQuantity <= 0 || inputQuantity > product.getStock()) {
			throw new CartException(CartErrorCode.INVALID_QUANTITY);
		}
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

	private Cart validateCart(Long cartId, Long userId){
		Cart cart = cartRepository.findByIdAndUserIdAndIsDeleted(cartId, userId, false);

		if (cart == null) {
			throw new CartException(CartErrorCode.THIS_CART_DOES_NOT_EXIST);
		}

		return cart;
	}

	private boolean existsInCart(Long userId, Long productId){
		return cartRepository.existsByUserIdAndProductIdAndIsDeleted(userId, productId, false);
	}
}

