package com.supercoding.commerce03.repository.order;

import com.supercoding.commerce03.repository.order.entity.Order;
import com.supercoding.commerce03.repository.order.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends
	JpaRepository<OrderDetail, Long> {

	List<OrderDetail> findOrderDetailsByOrder(Order order);


}
