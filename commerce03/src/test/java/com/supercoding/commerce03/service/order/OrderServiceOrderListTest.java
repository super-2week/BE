package com.supercoding.commerce03.service.order;

import com.supercoding.commerce03.repository.order.OrderDetailRepository;
import com.supercoding.commerce03.repository.order.OrderRepository;
import com.supercoding.commerce03.repository.order.entity.Order;
import com.supercoding.commerce03.repository.order.entity.OrderDetail;
import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.repository.user.UserRepository;
import com.supercoding.commerce03.repository.user.entity.User;
import com.supercoding.commerce03.service.order.exception.OrderErrorCode;
import com.supercoding.commerce03.service.order.exception.OrderException;
import com.supercoding.commerce03.web.dto.order.OrderDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class OrderServiceOrderListTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("주문 목록 가져오기 - 성공")
    void orderListSuccess() {
        //given
        Long userId = 1L;
        User user = User.builder()
                .userName("user1")
                .isDeleted(false)
                .build();
        user.setId(userId);
        Pageable pageable =PageRequest.of(0, 3);
        Order order1 = Order.builder()
                .orderedAt(LocalDateTime.now())
                .status("결제 완료")
                .address("testAddress")
                .phoneNumber("01012341234")
                .isDeleted(false)
                .totalAmount(35000)
                .createdAt(LocalDateTime.now())
                .postComment("testPostComment")
                .user(user)
                .id(1L)
                .build();
        Order order2 = Order.builder()
                .orderedAt(LocalDateTime.now())
                .status("결제 완료")
                .address("testAddress")
                .phoneNumber("01012341234")
                .isDeleted(false)
                .totalAmount(35000)
                .createdAt(LocalDateTime.now())
                .postComment("testPostComment")
                .user(user)
                .id(2L)
                .build();

        Product product1 = Product.builder()
                .id(1L)
                .store(null)
                .imageUrl("abc.url")
                .animalCategory(1)
                .productCategory(1)
                .productName("강아지사료")
                .stock(2)
                .price(15000)
                .purchaseCount(20)
                .build();

        Product product2 = Product.builder()
                .id(2L)
                .store(null)
                .imageUrl("abc.url")
                .animalCategory(2)
                .productCategory(1)
                .productName("강아지사료")
                .stock(50)
                .price(20000)
                .purchaseCount(20)
                .build();

        OrderDetail orderDetail1 = OrderDetail.builder()
                .amount(1)
                .order(order1)
                .price(20000)
                .isDeleted(false)
                .product(product1)
                .id(1L)
                .build();

        OrderDetail orderDetail2 = OrderDetail.builder()
                .order(order1)
                .amount(1)
                .price(20000)
                .isDeleted(false)
                .product(product2)
                .id(2L)
                .build();

        List<OrderDetail> orderDetails = Arrays.asList(
                orderDetail1, orderDetail2
        );
        List<Order> orderList = Arrays.asList(order1, order2);

        Page<Order> orders = new PageImpl<>(orderList, pageable, orderList.size());

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(orderRepository.findAllByUser(any(), any())).willReturn(orders);
        given(orderDetailRepository.findOrderDetailsByOrder(any())).willReturn(orderDetails);

        //when
        Page< OrderDto.OrderListResponse> orderListResponsePage = orderService.orderList(userId, pageable);

        //then
        assertEquals(1L, orderListResponsePage.getContent().get(0).getOrderId());
        assertEquals(2L, orderListResponsePage.getContent().get(1).getOrderId());

    }

    @Test
    @DisplayName("주문 목록 - 실패 : 유저 없음 ")
    void orderListFailedUserNotFound() {
        //given
        Long userId = 1L;

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        //when
        OrderException exception = assertThrows(OrderException.class,()-> orderService.orderList(userId, any()));

        //then
        assertEquals(OrderErrorCode.USER_NOT_FOUND, exception.getErrorCode());

    }
}
