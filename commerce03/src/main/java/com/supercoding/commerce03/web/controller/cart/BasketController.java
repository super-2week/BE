package com.supercoding.commerce03.web.controller.cart;

import com.supercoding.commerce03.repository.redis.Basket;
import com.supercoding.commerce03.service.cart.BasketService;
import com.supercoding.commerce03.service.security.Auth;
import com.supercoding.commerce03.service.security.AuthHolder;
import com.supercoding.commerce03.web.dto.cart.AddCart;
import com.supercoding.commerce03.web.dto.cart.ModifyCart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api/baskets")
@RestController
public class BasketController {

	private final BasketService basketService;

	@Auth
	@PostMapping
	public ResponseEntity<Basket> add(
			@RequestBody AddCart.Request request
	){
			Long userId = AuthHolder.getUserId();
			return ResponseEntity.ok(
					basketService.addBasket(userId, request)
			);
	}

	@Auth
	@GetMapping
	public ResponseEntity<Basket> get(
	){
			Long userId = AuthHolder.getUserId();
			return ResponseEntity.ok(
					basketService.getBasket(userId)
			);
	}

	@Auth
	@PutMapping
	public ResponseEntity<Basket> modify(
			@RequestBody ModifyCart.Request request
	){
		Long userId = AuthHolder.getUserId();
		return ResponseEntity.ok(
				basketService.modifyBasket(userId, request)
		);
	}

	@Auth
	@DeleteMapping("/{productId}")
	public ResponseEntity<Basket> remove(
			@PathVariable("productId") Long productId

	){
		Long userId = AuthHolder.getUserId();
		return ResponseEntity.ok(
				basketService.removeBasket(userId, productId)
		);
	}

}
