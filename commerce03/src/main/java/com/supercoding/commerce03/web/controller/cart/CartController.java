package com.supercoding.commerce03.web.controller.cart;

import com.supercoding.commerce03.service.cart.CartService;
import com.supercoding.commerce03.web.dto.cart.AddCart;
import com.supercoding.commerce03.web.dto.cart.GetCart;
import com.supercoding.commerce03.web.dto.cart.RemoveCart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api/carts")
@RestController
public class CartController {

	private final CartService cartService;

	@PostMapping
	public ResponseEntity<AddCart.Response> add(
			@RequestBody AddCart.Request request
	){
			Long userId = 1L;
			return ResponseEntity.ok(
					AddCart.Response.from(
							cartService.addToCart(request, userId)
					)
			);
	}

	@DeleteMapping
	public ResponseEntity<RemoveCart.Response> remove(
			@RequestBody RemoveCart.Request request
	){
			Long userId = 1L;
			return ResponseEntity.ok(
					RemoveCart.Response.from(
							cartService.removeFromCart(request, userId)
					)
			);
	}

	@GetMapping
	public ResponseEntity<Page<GetCart.Response>> get(
			Pageable pageable
	){
			Long userId = 1L;
			return ResponseEntity.ok(
					cartService.getCart(userId, pageable).map(GetCart.Response::from)
			);
	}

}
