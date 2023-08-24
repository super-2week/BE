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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class OrderServiceOrderViewDetailTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("주문 상세보기 - 성공")
    void OrderViewDetailSuccess() {
        Long userId = 1L;
        String orderId = "1";

        User user = User.builder()
                .userName("user1")
                .isDeleted(false)
                .build();
        user.setId(userId);

        Order order = Order.builder()
                .orderedAt(LocalDateTime.now())
                .recipient("recipientTest")
                .status("결제 완료")
                .address("testAddress")
                .phoneNumber("01012341234")
                .isDeleted(false)
                .totalAmount(35000)
                .createdAt(LocalDateTime.now())
                .postComment("testPostComment")
                .user(user)
                .build();
        order.setId(Long.valueOf(orderId));

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
                .order(order)
                .price(20000)
                .isDeleted(false)
                .product(product1)
                .id(1L)
                .build();

        OrderDetail orderDetail2 = OrderDetail.builder()
                .order(order)
                .amount(1)
                .price(20000)
                .isDeleted(false)
                .product(product2)
                .id(2L)
                .build();

        List<OrderDetail> orderDetails = Arrays.asList(
                orderDetail1, orderDetail2
        );

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
        given(orderDetailRepository.findOrderDetailsByOrder(any())).willReturn(orderDetails);

        //when
        OrderDto.OrderResponse orderResponse = orderService.orderViewDetail(userId, orderId);

        //then
        assertEquals("recipientTest", orderResponse.getRecipient());
        assertEquals("강아지사료", orderResponse.getOrderedProducts().get(0).getProductName());
        assertEquals("20,000", orderResponse.getOrderedProducts().get(0).getPrice());
    }

    @Test
    @DisplayName("주문 상세보기 - 실패 : 유저 없음")
    void OrderViewDetailFailedOrderNotFound() {

        Long userId = 1L;
        String orderId = "1";


        given(userRepository.findById(any())).willReturn(Optional.empty());

        //when
        OrderException exception = assertThrows(OrderException.class, ()->orderService.orderViewDetail(userId, orderId));

        //then
        assertEquals(OrderErrorCode.USER_NOT_FOUND, exception.getErrorCode());

    }
    @Test
    @DisplayName("주문 상세보기 - 실패 : 주문 없음")
    void OrderViewDetailFailedProductNotFound() {

        Long userId = 1L;
        String orderId = "1";

        User user = User.builder()
                .userName("user1")
                .isDeleted(false)
                .build();
        user.setId(userId);



        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(orderRepository.findById(any())).willReturn(Optional.empty());
        //when
        OrderException exception = assertThrows(OrderException.class, ()->orderService.orderViewDetail(userId, orderId));

        //then
        assertEquals(OrderErrorCode.ORDER_NOT_FOUND, exception.getErrorCode());

    }
    @Test
    @DisplayName("주문 상세보기 - 실패 : 상세보기 권한 없음")
    void OrderViewDetailFailedNoPermission() {

        Long userId = 1L;
        String orderId = "1";

        User user = User.builder()
                .userName("user1")
                .isDeleted(false)
                .build();
        user.setId(userId);

        User user2 = User.builder()
                .userName("user1")
                .isDeleted(false)
                .id(2L)
                .build();

        Order order = Order.builder()
                .orderedAt(LocalDateTime.now())
                .recipient("recipientTest")
                .status("결제 완료")
                .address("testAddress")
                .phoneNumber("01012341234")
                .isDeleted(false)
                .totalAmount(35000)
                .createdAt(LocalDateTime.now())
                .postComment("testPostComment")
                .user(user2)
                .build();
        order.setId(Long.valueOf(orderId));


        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(orderRepository.findById(any())).willReturn(Optional.of(order));
        //when
        OrderException exception = assertThrows(OrderException.class, ()->orderService.orderViewDetail(userId, orderId));

        //then
        assertEquals(OrderErrorCode.NO_PERMISSION_TO_VIEW, exception.getErrorCode());

    }

}
