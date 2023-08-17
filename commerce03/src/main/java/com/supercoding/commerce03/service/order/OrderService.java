package com.supercoding.commerce03.service.order;

import com.supercoding.commerce03.repository.order.OrderDetailRepository;
import com.supercoding.commerce03.repository.order.OrderRepository;
import com.supercoding.commerce03.repository.order.entity.Order;
import com.supercoding.commerce03.repository.order.entity.OrderDetail;
import com.supercoding.commerce03.repository.order.entity.ProductAndOrderAmount;
import com.supercoding.commerce03.repository.product.ProductRepository;
import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.repository.user.UserRepository;
import com.supercoding.commerce03.repository.user.entity.User;
import com.supercoding.commerce03.service.order.exception.OrderErrorCode;
import com.supercoding.commerce03.service.order.exception.OrderException;
import com.supercoding.commerce03.service.order.mapper.OrderMapper;
import com.supercoding.commerce03.web.dto.order.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public OrderDto.OrderRegisterResponse orderRegister(String userId, OrderDto.OrderRegisterRequest orderRegisterRequest) {
        //user userId로 객체 가져오기
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new OrderException(OrderErrorCode.USER_NOT_FOUND));

        // 주문 상품 총액 계산
        Integer totalAmount = orderRegisterRequest.getProducts().stream()
                .map(product -> product.getPrice() * product.getAmount())
                .mapToInt(Integer::intValue).sum();

        // orderRegisterRequest -> order entity 생성
        Order order = Order.builder()
                .orderedAt(LocalDateTime.now())
                .status("주문 중")
                .address(orderRegisterRequest.getAddress())
                .recipient(orderRegisterRequest.getRecipient())
                .phoneNumber(orderRegisterRequest.getPhoneNumber())
                .isDeleted(false)
                .totalAmount(totalAmount)
                .createdAt(LocalDateTime.now())
                .postComment(orderRegisterRequest.getPostComment())
                .user(user)

                .build();

        // order 테이블에 주문 정보 저장.
        orderRepository.save(order);

        // 상품들  -> orderDetails 생성 및 테이블에 저장
        //TODO : refactor 할 때 해당 로직 좀더 명확하게 구현해보자
        orderRegisterRequest.getProducts().stream()
                .forEach((product) -> {
                            OrderDetail orderDetail = OrderMapper.INSTANCE.productToOrderDetail(validProduct(product.getId()), order, product.getAmount());
                            log.info("orderDetail : " + orderDetail);
                            orderDetailRepository.save(orderDetail);
                        }
                );

        order.setStatus("결제 대기");


        // TODO : 저장 했다면 ? -> 결제 service 의 금액 차감(결제 로직)
//        금액 차감할 때, 금액 부족한 경우, => Exception // new OrderException(OrderErrorCode.LACK_OF_POINT));


        //  금액 차감 정상적으로 이루어졌다면? 물품 재고값 변경(재고 0보다 작아지면 재고 부족 Exception)
        List<ProductAndOrderAmount> productAndOrderAmounts
                = orderRegisterRequest.getProducts().stream()
                .map(orderProductRequest ->
                        ProductAndOrderAmount.builder()
                                .product(validProduct(orderProductRequest.getId()))
                                .amount(orderProductRequest.getAmount())
                                .build()
                ).collect(Collectors.toList());

        productAndOrderAmounts.stream()
                .forEach((productAndOrderAmount) -> {
                    Product productChangingAmount
                            = productRepository.findById(productAndOrderAmount.getProduct().getId())
                            .orElseThrow(() -> new OrderException(OrderErrorCode.PRODUCT_NOT_FOUND));
                    Integer stockToChange = productChangingAmount.getStock() - productAndOrderAmount.getAmount();
                    if (stockToChange >= 0) {
                        productChangingAmount.setStock(stockToChange);
                        productRepository.save(productChangingAmount);
                        order.setStatus("결제 완료");
                        log.info("stock : " + productChangingAmount.getStock());
                    } else {
                        //TODO : 결제 취소 로직 추가
                        throw new OrderException(OrderErrorCode.OUT_OF_STOCK);
                    }
                });

        // orderedProducts dto list 생성
        List<OrderDto.ResponseOrderProduct> orderedProducts = orderDetailRepository.findOrderDetailsByOrder(order).stream()
                .map(orderDetail -> OrderDto.ResponseOrderProduct.builder()
                        .id(orderDetail.getProduct().getId())
                        .productName(orderDetail.getProduct().getProductName())
                        .amount(orderDetail.getAmount())
                        .price(orderDetail.getProduct().getPrice())
                        .build()
                ).collect(Collectors.toList());

        // 반환할 OrderRegisterResponse 생성
        OrderDto.OrderRegisterResponse orderRegisterResponse = OrderDto.OrderRegisterResponse.builder()
                .orderedProducts(orderedProducts)
                .orderId(order.getId())
                .status(order.getStatus())
                .totalAmount(totalAmount)
                .recipient(order.getRecipient())
                .address(order.getAddress())
                .address(order.getAddress())
                .phoneNumber(order.getPhoneNumber())
                .postComment(order.getPostComment())
                .build();


        return orderRegisterResponse;
    }

    private Product validProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.PRODUCT_NOT_FOUND));
        return product;
    }

    @Transactional
    public OrderDto.OrderCancelResponse orderCancel(String userId, String orderId) {
        //user userId로 객체 가져오기
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new OrderException(OrderErrorCode.USER_NOT_FOUND));
        //Order 가져오기
        Long longOrderId = Long.valueOf(orderId);
        Order order = orderRepository.findById(longOrderId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND));

        // TODO : 주문 취소 로직
        order.setStatus("결제 취소");

        // 주문 취소 ( 주문 테이블 주문 취소, 주문 상세 삭제)
        List<OrderDetail> orderDetailsToDeleted = orderDetailRepository.findOrderDetailsByOrder(order);
        orderDetailsToDeleted.stream().forEach((orderDetail -> orderDetail.setIsDeleted(true)));
        order.setStatus("주문 취소");

        orderRepository.save(order);

        OrderDto.OrderCancelResponse orderCancelResponse
                = new OrderDto.OrderCancelResponse("주문이 정상적으로 취소 되었습니다.");

        return orderCancelResponse;

    }

    public Page<OrderDto.OrderListResponse> orderList(String userId,  Pageable pageable) {
        Long longUserId = Long.valueOf(userId);

        User user = userRepository.findById(longUserId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.USER_NOT_FOUND));

        // 에러가 잡힐줄 알았는데 안잡히네?!?!?
        Page<Order> orders = orderRepository.findAllByUser(user, pageable)
                .orElseThrow(() -> new OrderException(OrderErrorCode.NEVER_ORDERED_BEFORE));
        //가독성 위해 변경할 Refactoring 할 필요성. TODO
        Page<OrderDto.OrderListResponse> orderListResponsePage
                = orders.map(order-> OrderDto.OrderListResponse
                .builder()
                .status(order.getStatus())
                .orderedProducts(orderDetailRepository.findOrderDetailsByOrder(order).stream()
                        .map(orderDetail -> OrderDto.ResponseOrderProduct.builder()
                                .id(orderDetail.getProduct().getId())
                                .productName(orderDetail.getProduct().getProductName())
                                .amount(orderDetail.getAmount())
                                .price(orderDetail.getProduct().getPrice())
                                .imgUrl(orderDetail.getProduct().getImageUrl())
                                .build()
                        ).collect(Collectors.toList()))
                .orderedDate(order.getOrderedAt())
                .build() );

        return orderListResponsePage;
    }


}
