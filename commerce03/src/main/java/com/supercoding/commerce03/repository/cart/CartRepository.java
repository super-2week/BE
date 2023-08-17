package com.supercoding.commerce03.repository.cart;

import com.supercoding.commerce03.repository.cart.entity.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

	boolean existsByUserIdAndProductIdAndIsDeleted(Long userId, Long productId, Boolean isDeleted);

	Cart findByIdAndUserIdAndIsDeleted(Long cartId, Long userId, boolean isDeleted);

	Page<Cart> findAllByUserIdAndIsDeleted(Long userId, boolean isDeleted, Pageable pageable);
}
