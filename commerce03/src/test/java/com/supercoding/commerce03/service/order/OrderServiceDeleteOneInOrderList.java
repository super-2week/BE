package com.supercoding.commerce03.service.order;

import com.supercoding.commerce03.repository.order.OrderRepository;
import com.supercoding.commerce03.repository.order.entity.Order;
import com.supercoding.commerce03.repository.user.UserRepository;
import com.supercoding.commerce03.repository.user.entity.User;
import com.supercoding.commerce03.service.order.exception.OrderErrorCode;
import com.supercoding.commerce03.service.order.exception.OrderException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class OrderServiceDeleteOneInOrderList {
    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("주문 목록에서 주문내역 삭제하기 - 성공")
    void deleteOneInOrderListSuccess() {
        Long userId = 1L;
        String orderId = "1";

        User user = User.builder()
                .userName("user1")
                .isDeleted(false)
                .build();

        Order order = Order.builder()
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

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(orderRepository.findById(any())).willReturn(Optional.of(order));

        //when
        orderService.deleteOneInOrderList(userId, orderId);

        //then
        assertEquals(true, order.getIsDeleted());
    }

    @Test
    @DisplayName("주문 목록에서 주문내역 삭제하기 - 실패 : 유저 없음")
    void deleteOneInOrderListFailedNotFoundUser() {
        Long userId = 1L;
        String orderId = "1";

        given(userRepository.findById(any())).willReturn(Optional.empty());

        //when
        OrderException exception = assertThrows(OrderException.class,()->orderService.deleteOneInOrderList(userId, orderId));

        //then
        assertEquals(OrderErrorCode.USER_NOT_FOUND, exception.getErrorCode());

    }
    @Test
    @DisplayName("주문 목록에서 주문내역 삭제하기 - 실패 : 주문 없음")
    void deleteOneInOrderListFailedNotFoundOrder() {
        Long userId = 1L;
        String orderId = "1";

        User user = User.builder()
                .userName("user1")
                .isDeleted(false)
                .build();


        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(orderRepository.findById(any())).willReturn(Optional.empty());
        //when
        OrderException exception = assertThrows(OrderException.class,()->orderService.deleteOneInOrderList(userId, orderId));

        //then
        assertEquals(OrderErrorCode.ORDER_NOT_FOUND, exception.getErrorCode());

    }

    @Test
    @DisplayName("주문 목록에서 주문내역 삭제하기 - 실패 :삭제 권한 없음")
    void deleteOneInOrderListFailedNoPermission() {
        Long userId = 2L;
        String orderId = "1";

        User user = User.builder()
                .userName("user1")
                .isDeleted(false)
                .build();
        user.setId(1L);
        User user2 = User.builder()
                .userName("user1")
                .isDeleted(false)
                .id(2L)
                .build();

        Order order = Order.builder()
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


        given(userRepository.findById(any())).willReturn(Optional.of(user2));
        given(orderRepository.findById(any())).willReturn(Optional.of(order));
        //when
        OrderException exception = assertThrows(OrderException.class,()->orderService.deleteOneInOrderList(userId, orderId));

        //then
        assertEquals(OrderErrorCode.NO_PERMISSION_TO_DELETE, exception.getErrorCode());

    }

}
