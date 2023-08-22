package com.supercoding.commerce03.service.review;

import static com.supercoding.commerce03.repository.review.entity.Review.toEntity;

import com.supercoding.commerce03.repository.order.OrderRepository;
import com.supercoding.commerce03.repository.product.ProductRepository;
import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.repository.review.ReviewImageRepository;
import com.supercoding.commerce03.repository.review.ReviewRepository;
import com.supercoding.commerce03.repository.review.entity.Review;
import com.supercoding.commerce03.repository.review.entity.ReviewImage;
import com.supercoding.commerce03.repository.user.UserRepository;
import com.supercoding.commerce03.repository.user.entity.User;
import com.supercoding.commerce03.service.S3.S3Service;
import com.supercoding.commerce03.service.review.exception.ReviewErrorCode;
import com.supercoding.commerce03.service.review.exception.ReviewException;
import com.supercoding.commerce03.web.dto.review.CreateReview;
import com.supercoding.commerce03.web.dto.review.ModifyReview.Request;
import com.supercoding.commerce03.web.dto.review.ReviewDto;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {

	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final ReviewRepository reviewRepository;
	private final ReviewImageRepository reviewImageRepository;
	private final OrderRepository orderRepository;
	private final S3Service s3Service;

	@Transactional
	public ReviewDto createReview(CreateReview.Request request, Long userId,
			List<MultipartFile> multipartFile){

		Long inputProductId = request.getProductId();

		User validatedUser = validateUser(userId);
		Product validatedProduct = validateProduct(inputProductId);
		validateReviewAuthorization(userId, inputProductId);

		if (existReview(userId, inputProductId)) {
			throw new ReviewException(ReviewErrorCode.REVIEW_ALREADY_EXISTS);
		}

		Review review = toEntity(validatedUser, validatedProduct, request);
		reviewRepository.save(review);

		List<String> imageUrls = s3Service.uploadFiles(multipartFile);
		saveReviewImage(review, imageUrls);

		return ReviewDto.fromEntity(review);
	}

	@Transactional
	public ReviewDto modifyReview(Request request, Long userId, List<MultipartFile> multipartFile){

		Long inputReviewId = request.getReviewId();

		User validatedUser = validateUser(userId);
		Review validateReview = validateReview(inputReviewId);

		if (isNotReviewer(validatedUser.getId(), validateReview)) {
			throw new ReviewException(ReviewErrorCode.NO_PERMISSION_TO_UPDATE);
		}

		validateReview.setTitle(request.getTitle());
		validateReview.setContent(request.getContent());

		deleteReviewImage(validateReview);
		List<String> imageUrls = s3Service.uploadFiles(multipartFile);
		saveReviewImage(validateReview, imageUrls);

		return ReviewDto.fromEntity(validateReview);
	}

	@Transactional
	public ReviewDto deleteReview(Long reviewId, Long userId){

		User validatedUser = validateUser(userId);
		Review validateReview = validateReview(reviewId);

		if (isNotReviewer(validatedUser.getId(), validateReview)) {
			throw new ReviewException(ReviewErrorCode.NO_PERMISSION_TO_DELETE);
		}

		deleteReviewImage(validateReview);
		validateReview.setIsDeleted(true);

		return ReviewDto.fromEntity(validateReview);
	}

	public Page<ReviewDto> getReview(Long productId, Long cursor, Integer pageSize){

		validateProduct(productId);

		Page<Review> reviews = reviewRepository.findAllByProductIdAndIsDeletedWithCursor(
				productId, false, cursor, PageRequest.of(0, pageSize));
		return reviews.map(ReviewDto::fromEntity);
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

	private Review validateReview(Long reviewId){
		Review review = reviewRepository.findByIdAndIsDeleted(reviewId, false);
		if (review == null) {
			throw new ReviewException(ReviewErrorCode.REVIEW_DOES_NOT_EXIST);
		}
		return review;
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

	private boolean isNotReviewer(Long userId, Review review){
		return !Objects.equals(userId, review.getUser().getId());
	}

	private void saveReviewImage(Review review, List<String> imageUrls){
		for (String imageUrl : imageUrls) {
			reviewImageRepository.save(
					ReviewImage.builder()
							.review(review)
							.reviewImageUrl(imageUrl)
							.build());
		}
	}

	private void deleteReviewImage(Review review){
		List<ReviewImage> reviewImages = review.getReviewImages();
		for (ReviewImage reviewImage : reviewImages) {
			s3Service.deleteFile(reviewImage.getReviewImageUrl());
			reviewImageRepository.delete(reviewImage);
		}
	}
}
