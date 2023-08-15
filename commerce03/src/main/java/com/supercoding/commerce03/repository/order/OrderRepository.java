package com.supercoding.commerce03.repository.order;

import com.supercoding.commerce03.repository.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
