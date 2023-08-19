package com.supercoding.commerce03.web.dto.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class DeleteReview {

	@Setter
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class Response {

		private Long userId;
		private Long reviewId;
		private Long productId;

		public static Response from(ReviewDto reviewDto){
			return Response.builder()
					.userId(reviewDto.getUserId())
					.reviewId(reviewDto.getReviewId())
					.productId(reviewDto.getProductId())
					.build();
		}
	}

}
