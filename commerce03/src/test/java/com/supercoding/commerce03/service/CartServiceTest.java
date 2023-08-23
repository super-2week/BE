package com.supercoding.commerce03.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.supercoding.commerce03.repository.cart.CartRepository;
import com.supercoding.commerce03.repository.cart.entity.Cart;
import com.supercoding.commerce03.repository.product.ProductRepository;
import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.repository.store.entity.Store;
import com.supercoding.commerce03.repository.user.UserRepository;
import com.supercoding.commerce03.repository.user.entity.User;
import com.supercoding.commerce03.service.cart.CartService;
import com.supercoding.commerce03.service.cart.exception.CartErrorCode;
import com.supercoding.commerce03.service.cart.exception.CartException;
import com.supercoding.commerce03.web.dto.cart.AddCart;
import com.supercoding.commerce03.web.dto.cart.CartDto;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private CartRepository cartRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private CartService cartService;

	@Test
	void addToCartSuccess(){
		//given
		User user = User.builder()
				.userName("testName")
				.isDeleted(false)
				.build();
		user.setId(1L);

		Product product = Product.builder()
				.store(Store.builder()
						.storeName("testStoreName")
						.contact("testContact")
						.build())
				.animalCategory(1)
				.productCategory(1)
				.productName("testProductName")
				.modelNum("testModelNum")
				.originLabel("testOriginal")
				.price(10000)
				.description("testDescription")
				.stock(10)
				.wishCount(0)
				.purchaseCount(0)
				.createdAt(LocalDateTime.now())
				.build();
		product.setId(1L);

		given(userRepository.findById(anyLong()))
				.willReturn(Optional.of(user));
		given(productRepository.findById(anyLong()))
				.willReturn(Optional.of(product));
		given(cartRepository.save(any()))
				.willReturn(Cart.builder()
						.user(user)
						.product(product)
						.quantity(2)
						.isDeleted(false)
						.build());

		AddCart.Request request = AddCart.Request.builder()
				.productId(1L)
				.quantity(1)
				.build();

		ArgumentCaptor<Cart> captor = ArgumentCaptor.forClass(Cart.class);

		//when
		CartDto cartDto = cartService.addToCart(request, 1L);

		//then
		verify(cartRepository, times(1)).save(captor.capture());
		assertEquals(1, cartDto.getProductId());
		assertEquals(2, cartDto.getQuantity());
		assertEquals(1, captor.getValue().getQuantity());
		assertEquals(1, captor.getValue().getProduct().getId());
	}

	@Test
	@DisplayName("해당 유저 없음 - 장바구니 담기 실패")
	void addToCart_UserNotFound(){
		//given
		given(userRepository.findById(anyLong()))
				.willReturn(Optional.empty());

		AddCart.Request request = AddCart.Request.builder()
				.productId(1L)
				.quantity(1)
				.build();

		//when
		CartException exception = assertThrows(CartException.class,
				() -> cartService.addToCart(request, 1L));

		//then
		assertEquals(CartErrorCode.USER_NOT_FOUND, exception.getErrorCode());

	}
}
