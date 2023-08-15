package com.supercoding.commerce03.repository.cart;

import com.supercoding.commerce03.repository.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
