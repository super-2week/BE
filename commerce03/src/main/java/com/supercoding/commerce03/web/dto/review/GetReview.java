package com.supercoding.commerce03.web.dto.review;

import com.supercoding.commerce03.repository.review.entity.ReviewImage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class GetReview {

	@Setter
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class Response {

		private Long userId;
		private Long reviewId;
		private String title;
		private String content;
		private LocalDateTime createAt;
		private List<String> imageUrls;

		public static GetReview.Response from(ReviewDto reviewDto){
			return Response.builder()
					.userId(reviewDto.getUserId())
					.reviewId(reviewDto.getReviewId())
					.title(reviewDto.getTitle())
					.content(reviewDto.getContent())
					.createAt(reviewDto.getCreateAt())
					.imageUrls(reviewDto.getReviewImages().stream()
							.map(ReviewImage::getReviewImageUrl).collect(Collectors.toList()))
					.build();

		}
	}

}
