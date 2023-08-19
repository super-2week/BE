package com.supercoding.commerce03.web.dto.review;

import java.time.LocalDateTime;
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

		public static GetReview.Response from(ReviewDto reviewDto){
			return Response.builder()
					.userId(reviewDto.getUserId())
					.reviewId(reviewDto.getReviewId())
					.title(reviewDto.getTitle())
					.content(reviewDto.getContent())
					.createAt(reviewDto.getCreateAt())
					.build();

		}
	}

}
