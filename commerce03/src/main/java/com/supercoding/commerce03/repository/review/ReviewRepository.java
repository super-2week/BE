package com.supercoding.commerce03.repository.review;

import com.supercoding.commerce03.repository.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	boolean existsByUserIdAndProductIdAndIsDeleted(Long userId, Long productId, boolean isDeleted);

	Review findByIdAndIsDeleted(Long reviewId, Boolean isDeleted);

	@Query(
			"SELECT r FROM Review r WHERE r.product.id = :productId " +
					"AND r.isDeleted = :isDeleted " +
					"AND r.id > :cursor " +
					"ORDER BY r.id ASC "
	)
	Page<Review> findAllByProductIdAndIsDeletedWithCursor(Long productId, boolean isDeleted, Long cursor, PageRequest of);
}
