package com.supercoding.commerce03.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supercoding.commerce03.repository.redis.Basket;
import com.supercoding.commerce03.repository.redis.Words;
import com.supercoding.commerce03.service.cart.exception.CartErrorCode;
import com.supercoding.commerce03.service.cart.exception.CartException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisClient {

	private final RedisTemplate<String, Object> redisTemplate;
	private static final ObjectMapper mapper = new ObjectMapper();

	public <T> T get(Long key, Class<T> classType){
		return get(key.toString(), classType);
	}

	public <T> T getWords(String key, Class<T> classType){
		log.info("레디스가 연관검색어를 응답");
		return get(key, classType);
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

	public void putWords(String key, Words words) {
		try {
			redisTemplate.opsForValue().set(key, mapper.writeValueAsString(words));
		}catch(JsonProcessingException e){
			throw new RuntimeException("연관검색어를 레디스에 넣는 중 말로 형용할 수 없는 슬픈 일이 발생한 것 같습니다.");
		}
	}
}
