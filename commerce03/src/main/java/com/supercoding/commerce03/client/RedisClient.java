package com.supercoding.commerce03.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supercoding.commerce03.repository.redis.Basket;
import com.supercoding.commerce03.service.cart.exception.CartErrorCode;
import com.supercoding.commerce03.service.cart.exception.CartException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisClient {

	private final RedisTemplate<String, Object> redisTemplate;
	private static final ObjectMapper mapper = new ObjectMapper();

	public <T> T get(Long key, Class<T> classType){
		return get(key.toString(), classType);
	}

	private <T> T get(String key, Class<T> classType){
		String redisValue = (String) redisTemplate.opsForValue().get(key);
		if (ObjectUtils.isEmpty(redisValue)) {
			return null;
		} else {
			try {
				return mapper.readValue(redisValue, classType);
			} catch (JsonProcessingException e) {
				log.error("Parsing error", e);
				return null;
			}
		}
	}

	public void put(Long key, Basket basket){
		put(key.toString(), basket);
	}

	private void put(String key, Basket basket){
		try {
			redisTemplate.opsForValue().set(key, mapper.writeValueAsString(basket));
		} catch (JsonProcessingException e) {
			throw new CartException(CartErrorCode.PRODUCT_ALREADY_EXISTS);
		}
	}
}
