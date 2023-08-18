package com.supercoding.commerce03.repository.review;

import com.supercoding.commerce03.repository.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	boolean existsByUserIdAndProductIdAndIsDeleted(Long userId, Long productId, boolean isDeleted);
}
