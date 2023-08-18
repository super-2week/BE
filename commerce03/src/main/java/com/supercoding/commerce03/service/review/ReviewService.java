package com.supercoding.commerce03.service.review;

import com.supercoding.commerce03.repository.order.OrderRepository;
import com.supercoding.commerce03.repository.product.ProductRepository;
import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.repository.review.ReviewRepository;
import com.supercoding.commerce03.repository.review.entity.Review;
import com.supercoding.commerce03.repository.user.UserRepository;
import com.supercoding.commerce03.repository.user.entity.User;
import com.supercoding.commerce03.service.review.exception.ReviewErrorCode;
import com.supercoding.commerce03.service.review.exception.ReviewException;
import com.supercoding.commerce03.web.dto.review.CreateReview;
import com.supercoding.commerce03.web.dto.review.ReviewDto;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {

	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final ReviewRepository reviewRepository;
	private final OrderRepository orderRepository;

	@Transactional
	public ReviewDto createReview(CreateReview.Request request, Long userId){

		Long inputProductId = request.getProductId();

		User validatedUser = validateUser(userId);
		Product validatedProduct = validateProduct(inputProductId);
		validateReviewAuthorization(userId, inputProductId);

		if (existReview(userId, inputProductId)) {
			throw new ReviewException(ReviewErrorCode.REVIEW_ALREADY_EXISTS);
		}

		return ReviewDto.fromEntity(
				reviewRepository.save(
						Review.builder()
								.user(validatedUser)
								.product(validatedProduct)
								.title(request.getTitle())
								.content(request.getContent())
								.createAt(LocalDateTime.now())
								.isDeleted(false)
								.build()
				)
		);
	}

	private User validateUser(Long userId){
		return userRepository.findById(userId)
				.orElseThrow(() -> new ReviewException(ReviewErrorCode.USER_NOT_FOUND));
	}

	private Product validateProduct(Long productId){
		return productRepository.findById(productId)
				.orElseThrow(
						() -> new ReviewException(ReviewErrorCode.THIS_PRODUCT_DOES_NOT_EXIST));
	}

	private boolean existReview(Long userId, Long productId){
		return reviewRepository.existsByUserIdAndProductIdAndIsDeleted(userId, productId, false);
	}

	private void validateReviewAuthorization(Long userId, Long productId){
		Integer count =
				orderRepository.countByUserIdAndProductIdAndIsDeleted(userId, productId, false);
		if (count <= 0) {
			throw new ReviewException(ReviewErrorCode.REVIEW_PERMISSION_DENIED);
		}
	}
}
