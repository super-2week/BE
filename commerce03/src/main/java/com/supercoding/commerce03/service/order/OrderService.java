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
                .forEach((productAndOrderAmount)->{
                    Product productChangingAmount
                            = productRepository.findById(productAndOrderAmount.getProduct().getId())
                            .orElseThrow(()->new OrderException(OrderErrorCode.PRODUCT_NOT_FOUND));
                    Integer stockToChange = productChangingAmount.getStock() - productAndOrderAmount.getAmount();
                    if(stockToChange >=0){
                        productChangingAmount.setStock(stockToChange);
                        productRepository.save(productChangingAmount);
                        order.setStatus("결제 완료");
                        log.info("stock : "+productChangingAmount.getStock());
                    }else{
                        //TODO : 결제 취소 로직 추가
                        throw new OrderException(OrderErrorCode.OUT_OF_STOCK);
                    }
                });

        // orderedProducts dto list 생성
        List<OrderDto.OrderProductResponse> orderedProducts = orderDetailRepository.findOrderDetailsByOrder(order).stream()
                .map(orderDetail -> OrderDto.OrderProductResponse.builder()
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


}
