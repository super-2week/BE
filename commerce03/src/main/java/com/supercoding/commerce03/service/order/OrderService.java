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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private final PaymentService paymentService;

    @Transactional
    public OrderDto.OrderResponse orderRegister(String userId, OrderDto.OrderRegisterRequest orderRegisterRequest) {
        //user userId로 객체 가져오기
        User user = validUser(Long.valueOf(userId));

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
        orderRegisterRequest.getProducts().stream()
                .forEach((product) -> {
                            OrderDetail orderDetail
                                    = OrderDetail.builder()
                                    .amount(product.getAmount())
                                    .order(order)
                                    .price(product.getPrice())
                                    .isDeleted(false)
                                    .product(validProduct(product.getId()))
                                    .build();
                            orderDetailRepository.save(orderDetail);
                        }
                );

        order.setStatus("결제 대기");


        // TODO : 저장 했다면 ? -> 결제 service 의 금액 차감(결제 로직)
//        금액 차감할 때, 금액 부족한 경우, => Exception // new OrderException(OrderErrorCode.LACK_OF_POINT));
        paymentService.orderByBusiness(Long.valueOf(userId), totalAmount);


        //  물품 재고값 변경(재고 0보다 작아지면 재고 부족 Exception) + 구매 횟수 변경
        List<OrderDetail> orderDetails = orderDetailRepository.findOrderDetailsByOrder(order);
        orderDetails.stream().forEach((orderDetail) -> {
            Product productChangingAmount
                    = productRepository.findById(orderDetail.getProduct().getId())
                    .orElseThrow(() -> new OrderException(OrderErrorCode.PRODUCT_NOT_FOUND));
            Integer stockToChange = productChangingAmount.getStock() - orderDetail.getAmount();
            Integer purchaseCountToChange = productChangingAmount.getPurchaseCount() + orderDetail.getAmount();
            if (stockToChange >= 0) {
                productChangingAmount.setStock(stockToChange);
                productChangingAmount.setPurchaseCount(purchaseCountToChange);
                productRepository.save(productChangingAmount);
                order.setStatus("결제 완료");
                log.info("stock : " + productChangingAmount.getStock());
            } else {

                //TODO : 결제 취소 로직 추가
                paymentService.cancelByBusiness(Long.valueOf(userId), totalAmount);
                throw new OrderException(OrderErrorCode.OUT_OF_STOCK);
            }
        });


        // orderedProducts dto list 생성
        List<OrderDto.ResponseOrderProduct> orderedProducts = orderDetails.stream()
                .map(orderDetail -> OrderDto.ResponseOrderProduct.builder()
                        .id(orderDetail.getProduct().getId())
                        .productName(orderDetail.getProduct().getProductName())
                        .amount(orderDetail.getAmount())
                        .price(orderDetail.getProduct().getPrice())
                        .imgUrl(orderDetail.getProduct().getImageUrl())
                        .build()
                ).collect(Collectors.toList());

        // 반환할 OrderRegisterResponse 생성
        OrderDto.OrderResponse orderRegisterResponse = OrderDto.OrderResponse.builder()
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


    @Transactional
    public OrderDto.OrderCancelResponse orderCancel(String userId, String orderId) {
        //user userId로 객체 가져오기
        User user = validUser(Long.valueOf(userId));
        //Order 가져오기
        Order order = validOrder(Long.valueOf(orderId));

        if (order.getStatus().equals("주문 취소")) throw new OrderException(OrderErrorCode.ORDER_ALREADY_CANCELED);

        // TODO : 결제 취소 로직
        paymentService.cancelByBusiness(user.getId(), order.getTotalAmount());
        order.setStatusAndTimeNow("결제 취소");

        // 주문 취소 ( 주문 테이블 주문 취소, 주문 상세 삭제)
        List<OrderDetail> orderDetailsToDeleted = orderDetailRepository.findOrderDetailsByOrder(order);
        orderDetailsToDeleted.stream().forEach((orderDetail) -> {
            orderDetail.setIsDeleted(true);
            Product productChangingAmount
                    = productRepository.findById(orderDetail.getProduct().getId())
                    .orElseThrow(() -> new OrderException(OrderErrorCode.PRODUCT_NOT_FOUND));
            Integer stockToChange = productChangingAmount.getStock() + orderDetail.getAmount();
            Integer purchaseCountToChange = productChangingAmount.getPurchaseCount() - orderDetail.getAmount();

            productChangingAmount.setStock(stockToChange);
            productChangingAmount.setPurchaseCount(purchaseCountToChange);
            productRepository.save(productChangingAmount);
            order.setStatus("결제 완료");
            log.info("stock : " + productChangingAmount.getStock());
        });
        order.setStatusAndTimeNow("주문 취소");


        orderRepository.save(order);

        OrderDto.OrderCancelResponse orderCancelResponse
                = new OrderDto.OrderCancelResponse("주문이 정상적으로 취소 되었습니다.");

        return orderCancelResponse;

    }

    public Page<OrderDto.OrderListResponse> orderList(String userId, Pageable pageable) {

        User user = validUser(Long.valueOf(userId));

        Page<Order> orders = orderRepository.findAllByUser(user, pageable);

        Page<OrderDto.OrderListResponse> orderListResponsePage
                = orders.map(order -> OrderDto.OrderListResponse
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
                .build());

        return orderListResponsePage;
    }

    @Transactional
    public String deleteOneInOrderList(String orderId) {
        Order orderToDeleted = validOrder(Long.valueOf(orderId));

        if (orderToDeleted.getIsDeleted() == true) throw new OrderException(OrderErrorCode.ORDER_ALREADY_DELELED);

        orderToDeleted.setIsDeleted(true);
        orderToDeleted.setModifiedAt(LocalDateTime.now());

        orderRepository.save(orderToDeleted);

        return "요청하신 orderId " + orderId + "의 주문 내역이 삭제되었습니다.";
    }

    public OrderDto.OrderResponse orderViewDetail(String orderId) {
        Order order = validOrder(Long.valueOf(orderId));

        OrderDto.OrderResponse orderRegisterResponse
                = OrderDto.OrderResponse.builder()
                .orderId(Long.valueOf(orderId))
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .recipient(order.getRecipient())
                .address(order.getAddress())
                .phoneNumber(order.getPhoneNumber())
                .postComment(order.getPostComment())
                .orderedProducts(orderDetailRepository.findOrderDetailsByOrder(order)
                        .stream()
                        .map(orderDetail -> OrderDto.ResponseOrderProduct
                                .builder()
                                .id(orderDetail.getId())
                                .productName(orderDetail.getProduct().getProductName())
                                .price(orderDetail.getPrice())
                                .amount(orderDetail.getAmount())
                                .imgUrl(orderDetail.getProduct().getImageUrl())
                                .build()
                        ).collect(Collectors.toList())
                )
                .build();

        return orderRegisterResponse;

    }

    private User validUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.USER_NOT_FOUND));
    }

    private Product validProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.PRODUCT_NOT_FOUND));
        return product;
    }

    private Order validOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND));
    }


}
