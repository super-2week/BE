package com.supercoding.commerce03.repository.order;

import com.supercoding.commerce03.repository.order.entity.Order;
import com.supercoding.commerce03.repository.user.entity.User;
import com.supercoding.commerce03.web.dto.order.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Page<Order>> findAllByUser(User user, Pageable pageable);

}
