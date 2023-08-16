package com.supercoding.commerce03.repository.wish;

import com.supercoding.commerce03.repository.store.entity.Store;
import com.supercoding.commerce03.repository.wish.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Long> {
    boolean existsByUserIdAndProductId(Long userId, Long productId);

    List<Wish> findByUser_Id(Long userId);
}
