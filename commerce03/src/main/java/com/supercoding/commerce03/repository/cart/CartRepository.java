package com.supercoding.commerce03.repository.cart;

import com.supercoding.commerce03.repository.cart.entity.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<Cart, Long> {

	boolean existsByUserIdAndProductIdAndIsDeleted(Long userId, Long productId, Boolean isDeleted);

	Cart findByIdAndUserIdAndIsDeleted(Long cartId, Long userId, boolean isDeleted);

	@Query(
			"SELECT c FROM Cart c WHERE c.user.id = :userId " +
			"AND c.isDeleted = :isDeleted " +
			"AND c.id > :cursor " +
			"ORDER BY c.id ASC "
	)
	Page<Cart> findAllByUserIdAndIsDeletedWithCursor(Long userId, boolean isDeleted, Long cursor, Pageable pageable);
}
