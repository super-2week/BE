package com.supercoding.commerce03.repository.order;

import com.supercoding.commerce03.repository.order.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends
	JpaRepository<OrderDetail, Long> {

}
