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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	@DeleteMapping("/{cartId}")
	public ResponseEntity<RemoveCart.Response> remove(
			@PathVariable("cartId") Long cartId
	){
			Long userId = 1L;
			return ResponseEntity.ok(
					RemoveCart.Response.from(
							cartService.removeFromCart(cartId, userId)
					)
			);
	}

	@GetMapping
	public ResponseEntity<Page<GetCart.Response>> get(
			@RequestParam(defaultValue = "0") Long cursor,
			@RequestParam(defaultValue = "10") Integer pageSize
	){
			Long userId = 1L;
			return ResponseEntity.ok(
					cartService.getCart(userId, cursor, pageSize).map(GetCart.Response::from)
			);
	}

}
