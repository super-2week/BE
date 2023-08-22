package com.supercoding.commerce03.repository.wish;

import com.supercoding.commerce03.repository.store.entity.Store;
import com.supercoding.commerce03.repository.wish.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {
    boolean existsByUserIdAndProductId(Long userId, Long productId);

    List<Wish> findByUserId(Long userId);

    Optional<Wish> findByUserIdAndProductId(Long userId, Long productId);
}
