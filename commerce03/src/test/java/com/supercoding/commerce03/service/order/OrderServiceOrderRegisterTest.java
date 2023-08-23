package com.supercoding.commerce03.service.order;

import com.supercoding.commerce03.repository.order.OrderDetailRepository;
import com.supercoding.commerce03.repository.order.OrderRepository;
import com.supercoding.commerce03.repository.order.entity.Order;
import com.supercoding.commerce03.repository.order.entity.OrderDetail;
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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


@Slf4j
@ExtendWith(MockitoExtension.class)
class OrderServiceOrderRegisterTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private OrderService orderService;



    @Test
    @DisplayName("주문하기")
    void orderRegisterSuccess() {
        //given
        Long userId = 1L;

        User user = User.builder()
                .userName("user1")
                .isDeleted(false)
                .isDeleted(false)
                .build();
        user.setId(userId);

        Product product1 = Product.builder()
                .id(1L)
                .store(null)
                .imageUrl("abc.url")
                .animalCategory(1)
                .productCategory(1)
                .productName("강아지사료")
                .stock(2)
                .price(15000)
                .purchaseCount(0)
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
                .purchaseCount(0)
                .build();

        OrderDto.RequestOrderProduct orderProduct1 =
                OrderDto.RequestOrderProduct.builder()
                        .id(product1.getId())
                        .price(product1.getPrice())
                        .amount(1)
                        .build();

        OrderDto.RequestOrderProduct orderProduct2 =
                OrderDto.RequestOrderProduct.builder()
                        .id(product2.getId())
                        .price(product2.getPrice())
                        .amount(1)
                        .build();
        List<OrderDto.RequestOrderProduct> orderProductList =
                Arrays.asList(orderProduct1, orderProduct2);



        OrderDto.OrderRegisterRequest orderRegisterRequest =
                OrderDto.OrderRegisterRequest.builder()
                        .recipient("recipient1")
                        .address("addressSomeWhere")
                        .phoneNumber("01012341234")
                        .postComment("post comment")
                        .products(orderProductList)
                        .build();

        Order order = Order.builder()
                .orderedAt(LocalDateTime.now())
                .status("주문 중")
                .address(orderRegisterRequest.getAddress())
                .phoneNumber(orderRegisterRequest.getPhoneNumber())
                .isDeleted(false)
                .totalAmount(35000)
                .createdAt(LocalDateTime.now())
                .postComment(orderRegisterRequest.getPostComment())
                .user(user)
                .build();
        order.setId(1L);



        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(orderRepository.save(any()))
                .willReturn(order);
        OrderDetail orderDetail1 = OrderDetail.builder()
                .amount(orderProduct1.getAmount())
                .order(order)
                .price(orderProduct1.getPrice())
                .isDeleted(false)
                .product(product1)
                .id(1L)
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .order(order)
                .amount(orderProduct2.getAmount())
                .price(orderProduct2.getPrice())
                .isDeleted(false)
                .product(product2)
                .id(2L)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product2));


        when(orderDetailRepository.save(any(OrderDetail.class))).thenAnswer(new Answer<OrderDetail>() {
            @Override
            public OrderDetail answer(InvocationOnMock invocation) throws Throwable {
                OrderDetail savedOrderDetail = invocation.getArgument(0);
                if (savedOrderDetail.getProduct().getId() == 1L) {
                    return orderDetail1;
                } else {
                    return orderDetail2;
                }
            }
        });


        //when
        OrderDto.OrderResponse orderResponse = orderService.orderRegister(userId, orderRegisterRequest);


        //then
        assertEquals(1, orderResponse.getOrderId());
        assertEquals("결제 완료", orderResponse.getStatus());
        assertEquals("35,000", orderResponse.getTotalAmount());
        assertThat(orderResponse.getOrderedProducts().get(0).getId()).isEqualTo(1);
        assertThat(orderResponse.getOrderedProducts().get(0).getPrice()).isEqualTo("15,000");
        assertThat(orderResponse.getOrderedProducts().get(1).getId()).isEqualTo(2);
        assertThat(orderResponse.getOrderedProducts().get(1).getPrice()).isEqualTo("20,000");

    }

    @Test
    @DisplayName("주문하기 - 유저 없음: 주문 실패 ")
    void orderRegisterFailedUserNotFound() {
        //given
        Long userId = 1L;
        OrderDto.OrderRegisterRequest orderRegisterRequest =
                OrderDto.OrderRegisterRequest.builder()
                        .recipient("recipient1")
                        .address("addressSomeWhere")
                        .phoneNumber("01012341234")
                        .postComment("post comment")
                        .products(null)
                        .build();

        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        OrderException exception = assertThrows(OrderException.class, () -> orderService.orderRegister(userId, orderRegisterRequest));

        //then
        assertEquals(OrderErrorCode.USER_NOT_FOUND,exception.getErrorCode());

    }

    @Test
    @DisplayName("주문하기 - 상품 없음: 주문 실패 ")
    void orderRegisterFailedProductNotFound() {
        //given
        Long userId = 1L;
        User user = User.builder()
                .userName("user1")
                .isDeleted(false)
                .isDeleted(false)
                .build();
        user.setId(userId);
        Product product1 = Product.builder()
                .id(1L)
                .store(null)
                .imageUrl("abc.url")
                .animalCategory(1)
                .productCategory(1)
                .productName("강아지사료")
                .stock(2)
                .price(15000)
                .purchaseCount(0)
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
                .purchaseCount(0)
                .build();

        OrderDto.RequestOrderProduct orderProduct1 =
                OrderDto.RequestOrderProduct.builder()
                        .id(product1.getId())
                        .price(product1.getPrice())
                        .amount(1)
                        .build();

        OrderDto.RequestOrderProduct orderProduct2 =
                OrderDto.RequestOrderProduct.builder()
                        .id(product2.getId())
                        .price(product2.getPrice())
                        .amount(1)
                        .build();
        List<OrderDto.RequestOrderProduct> orderProductList =
                Arrays.asList(orderProduct1, orderProduct2);



        OrderDto.OrderRegisterRequest orderRegisterRequest =
                OrderDto.OrderRegisterRequest.builder()
                        .recipient("recipient1")
                        .address("addressSomeWhere")
                        .phoneNumber("01012341234")
                        .postComment("post comment")
                        .products(orderProductList)
                        .build();
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(productRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        OrderException exception = assertThrows(OrderException.class, () -> orderService.orderRegister(userId, orderRegisterRequest));

        //then
        assertEquals(OrderErrorCode.PRODUCT_NOT_FOUND,exception.getErrorCode());

    }
    @Test
    @DisplayName("주문하기 - 재고 없음: 주문 실패 ")
    void orderRegisterFailedOutOfStock() {
        //given
        Long userId = 1L;
        User user = User.builder()
                .userName("user1")
                .isDeleted(false)
                .isDeleted(false)
                .build();
        user.setId(userId);
        Product product1 = Product.builder()
                .id(1L)
                .store(null)
                .imageUrl("abc.url")
                .animalCategory(1)
                .productCategory(1)
                .productName("강아지사료")
                .stock(0)
                .price(15000)
                .purchaseCount(0)
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
                .purchaseCount(0)
                .build();

        OrderDto.RequestOrderProduct orderProduct1 =
                OrderDto.RequestOrderProduct.builder()
                        .id(product1.getId())
                        .price(product1.getPrice())
                        .amount(1)
                        .build();

        OrderDto.RequestOrderProduct orderProduct2 =
                OrderDto.RequestOrderProduct.builder()
                        .id(product2.getId())
                        .price(product2.getPrice())
                        .amount(1)
                        .build();
        List<OrderDto.RequestOrderProduct> orderProductList =
                Arrays.asList(orderProduct1, orderProduct2);



        OrderDto.OrderRegisterRequest orderRegisterRequest =
                OrderDto.OrderRegisterRequest.builder()
                        .recipient("recipient1")
                        .address("addressSomeWhere")
                        .phoneNumber("01012341234")
                        .postComment("post comment")
                        .products(orderProductList)
                        .build();

        Order order = Order.builder()
                .orderedAt(LocalDateTime.now())
                .status("주문 중")
                .address(orderRegisterRequest.getAddress())
                .phoneNumber(orderRegisterRequest.getPhoneNumber())
                .isDeleted(false)
                .totalAmount(35000)
                .createdAt(LocalDateTime.now())
                .postComment(orderRegisterRequest.getPostComment())
                .user(user)
                .build();
        order.setId(1L);


        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(orderRepository.save(any()))
                .willReturn(order);
        OrderDetail orderDetail1 = OrderDetail.builder()
                .amount(orderProduct1.getAmount())
                .order(order)
                .price(orderProduct1.getPrice())
                .isDeleted(false)
                .product(product1)
                .id(1L)
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .order(order)
                .amount(orderProduct2.getAmount())
                .price(orderProduct2.getPrice())
                .isDeleted(false)
                .product(product2)
                .id(2L)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product2));


        when(orderDetailRepository.save(any(OrderDetail.class))).thenAnswer(new Answer<OrderDetail>() {
            @Override
            public OrderDetail answer(InvocationOnMock invocation) throws Throwable {
                OrderDetail savedOrderDetail = invocation.getArgument(0);
                if (savedOrderDetail.getProduct().getId() == 1L) {
                    return orderDetail1;
                } else {
                    return orderDetail2;
                }
            }
        });

        //when
        OrderException exception = assertThrows(OrderException.class, () -> orderService.orderRegister(userId, orderRegisterRequest));

        //then
        assertEquals(OrderErrorCode.OUT_OF_STOCK,exception.getErrorCode());

    }




}