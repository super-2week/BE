package com.supercoding.commerce03.repository.wish;

import com.supercoding.commerce03.repository.store.entity.Store;
import com.supercoding.commerce03.repository.wish.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long> {
}
