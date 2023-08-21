package com.supercoding.commerce03.web.dto.review;

import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.repository.review.entity.Review;
import com.supercoding.commerce03.repository.review.entity.ReviewImage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {

	private Long userId;
	private Long productId;
	private Long reviewId;
	private String title;
	private String content;
	private LocalDateTime createAt;
	private List<ReviewImage> reviewImages = new ArrayList<>();


	public static ReviewDto fromEntity(Review review){
		Product product = review.getProduct();
		return ReviewDto.builder()
				.userId(review.getUser().getId())
				.productId(product.getId())
				.reviewId(review.getId())
				.title(review.getTitle())
				.content(review.getTitle())
				.createAt(review.getCreateAt())
				.reviewImages(review.getReviewImages())
				.build();
	}
}
