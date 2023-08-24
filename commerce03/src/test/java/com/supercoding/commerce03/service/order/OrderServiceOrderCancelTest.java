package com.supercoding.commerce03.service.order;

import com.supercoding.commerce03.repository.order.OrderDetailRepository;
import com.supercoding.commerce03.repository.order.OrderRepository;
import com.supercoding.commerce03.repository.order.entity.Order;
import com.supercoding.commerce03.repository.order.entity.OrderDetail;
import com.supercoding.commerce03.repository.payment.PaymentDetailRepository;
import com.supercoding.commerce03.repository.payment.PaymentRepository;
import com.supercoding.commerce03.repository.payment.entity.BusinessByType;
import com.supercoding.commerce03.repository.payment.entity.Payment;
import com.supercoding.commerce03.repository.payment.entity.PaymentDetail;
import com.supercoding.commerce03.repository.product.ProductRepository;
import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.repository.user.UserRepository;
import com.supercoding.commerce03.repository.user.entity.User;
import com.supercoding.commerce03.service.order.exception.OrderErrorCode;
import com.supercoding.commerce03.service.order.exception.OrderException;
import com.supercoding.commerce03.service.payment.PaymentService;
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
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class OrderServiceOrderCancelTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private PaymentService paymentService;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("주문취소 -성공 ")
    void cancelOrderSuccess(){
        //given
        Long userId = 1L;
        String orderId = "1";

        User user = User.builder()
                .userName("user1")
                .isDeleted(false)
                .build();
        user.setId(userId);

        Order order = Order.builder()
                .orderedAt(LocalDateTime.now())
                .status("주문 중")
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

        given(productRepository.findById(1L)).willReturn(Optional.of(product1));
        given(productRepository.findById(2L)).willReturn(Optional.of(product2));

        //when
        orderService.orderCancel(userId, orderId);

        //then
        assertEquals(true, orderDetail1.getIsDeleted());
        assertEquals(true, orderDetail2.getIsDeleted());
        assertEquals("주문 취소", order.getStatus());

        verify(paymentService, times(1)).cancelByBusiness(eq(userId), eq(order.getTotalAmount()));

    }
    @Test
    @DisplayName("주문취소 - 실패 : 유저 없음")
    void orderCancelFailedUserNotFound() {
        //given
        Long userId = 1L;
        String orderId = "1";

        given(userRepository.findById(any())).willReturn(Optional.empty());

        //when
        OrderException exception = assertThrows(OrderException.class, ()->orderService.orderCancel(userId, orderId));

        //then
        assertEquals(OrderErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        verify(paymentService, never()).cancelByBusiness(any(), any());

    }
    @Test
    @DisplayName("주문취소 - 실패 : 주문 없음")
    void orderCancelFailedOrderNotFound() {
        //given
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
        OrderException exception = assertThrows(OrderException.class, ()->orderService.orderCancel(userId, orderId));

        //then
        assertEquals(OrderErrorCode.ORDER_NOT_FOUND, exception.getErrorCode());
        verify(paymentService, never()).cancelByBusiness(any(), any());

    }
    @Test
    @DisplayName("주문취소 - 실패 : 상품 없음")
    void orderCancelFailedProductNotFound() {
        //given
        Long userId = 1L;
        String orderId = "1";

        User user = User.builder()
                .userName("user1")
                .isDeleted(false)
                .build();
        user.setId(userId);

        Order order = Order.builder()
                .orderedAt(LocalDateTime.now())
                .status("주문 중")
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

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(orderRepository.findById(any())).willReturn(Optional.of(order));
        given(orderDetailRepository.findOrderDetailsByOrder(any())).willReturn(orderDetails);
        given(productRepository.findById(any())).willReturn(Optional.empty());
        //when
        OrderException exception = assertThrows(OrderException.class, ()->orderService.orderCancel(userId, orderId));

        //then
        assertEquals(OrderErrorCode.PRODUCT_NOT_FOUND, exception.getErrorCode());
        verify(paymentService, never()).cancelByBusiness(any(), any());

    }

}
