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
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
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


    private DecimalFormat df = new DecimalFormat("###,###");

    @Transactional
    public OrderDto.OrderResponse orderRegister(Long userId, OrderDto.OrderRegisterRequest orderRegisterRequest) {
        //user userId로 객체 가져오기
        User user = validUser(userId);

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
        List<OrderDetail> orderDetails = orderRegisterRequest.getProducts().stream().map(product->orderDetailRepository.save(
                OrderDetail.builder()
                        .amount(product.getAmount())
                        .order(order)
                        .price(product.getPrice())
                        .isDeleted(false)
                        .product(validProduct(product.getId()))
                        .build()
        )).collect(Collectors.toList());

        order.setStatus("결제 대기");


        // TODO : 저장 했다면 ? -> 결제 service 의 금액 차감(결제 로직)
//        금액 차감할 때, 금액 부족한 경우, => Exception // new OrderException(OrderErrorCode.LACK_OF_POINT));
        paymentService.orderByBusiness(userId, totalAmount);

        //  물품 재고값 변경(재고 0보다 작아지면 재고 부족 Exception) + 구매 횟수 변경
        decreaseStockAndIncreasePurchaseAmount(orderDetails, order, userId, totalAmount);


        // orderedProducts dto list 생성
        List<OrderDto.ResponseOrderProduct> orderedProducts = orderDetails.stream()
                .map(orderDetail -> OrderDto.ResponseOrderProduct.builder()
                        .id(orderDetail.getProduct().getId())
                        .productName(orderDetail.getProduct().getProductName())
                        .amount(orderDetail.getAmount())
                        .price(df.format(orderDetail.getProduct().getPrice()))
                        .imgUrl(orderDetail.getProduct().getImageUrl())
                        .build()
                ).collect(Collectors.toList());

        // 반환할 OrderRegisterResponse 생성
        OrderDto.OrderResponse orderRegisterResponse = OrderDto.OrderResponse.builder()
                .orderedProducts(orderedProducts)
                .orderId(order.getId())
                .status(order.getStatus())
                .totalAmount(df.format(totalAmount))
                .recipient(order.getRecipient())
                .address(order.getAddress())
                .address(order.getAddress())
                .phoneNumber(order.getPhoneNumber())
                .postComment(order.getPostComment())
                .build();


        return orderRegisterResponse;
    }


    @Transactional
    public OrderDto.OrderCancelResponse orderCancel(Long userId, String orderId) {
        //user userId로 객체 가져오기
        User user = validUser(userId);
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


        OrderDto.OrderCancelResponse orderCancelResponse
                = new OrderDto.OrderCancelResponse("주문이 정상적으로 취소 되었습니다.");

        return orderCancelResponse;

    }

    public Page<OrderDto.OrderListResponse> orderList(Long userId, Pageable pageable) {

        User user = validUser(userId);

        Page<Order> orders = orderRepository.findAllByUser(user, pageable);

        Page<OrderDto.OrderListResponse> orderListResponsePage
                = orders.map(order -> OrderDto.OrderListResponse
                .builder()
                .orderId(order.getId())
                .status(order.getStatus())
                .orderedProducts(orderDetailRepository.findOrderDetailsByOrder(order).stream()
                        .map(orderDetail -> OrderDto.ResponseOrderProduct.builder()
                                .id(orderDetail.getProduct().getId())
                                .productName(orderDetail.getProduct().getProductName())
                                .amount(orderDetail.getAmount())
                                .price(df.format(orderDetail.getProduct().getPrice()))
                                .imgUrl(orderDetail.getProduct().getImageUrl())
                                .build()
                        ).collect(Collectors.toList()))
                .orderedDate(order.getOrderedAt())
                .build());

        return orderListResponsePage;
    }

    @Transactional
    public String deleteOneInOrderList(Long userId, String orderId) {
        User user = validUser(userId);
        Order orderToDeleted = validOrder(Long.valueOf(orderId));

        if (orderToDeleted.getIsDeleted() == true) throw new OrderException(OrderErrorCode.ORDER_ALREADY_DELELED);
        if (!user.equals(orderToDeleted.getUser())) {
            throw new OrderException(OrderErrorCode.NO_PERMISSION_TO_DELETE);
        }

        orderToDeleted.setIsDeleted(true);
        orderToDeleted.setModifiedAt(LocalDateTime.now());


        return "요청하신 orderId " + orderId + "의 주문 내역이 삭제되었습니다.";
    }

    public OrderDto.OrderResponse orderViewDetail(Long userId, String orderId) {
        Order order = validOrder(Long.valueOf(orderId));
        User user = validUser(userId);
        if (!user.equals(order.getUser())) {
            throw new OrderException(OrderErrorCode.NO_PERMISSION_TO_VIEW);
        }

        OrderDto.OrderResponse orderRegisterResponse
                = OrderDto.OrderResponse.builder()
                .orderId(Long.valueOf(orderId))
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount().toString())
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
                                .price(df.format(orderDetail.getPrice()))
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

    @Transactional
    public void decreaseStockAndIncreasePurchaseAmount(List<OrderDetail> orderDetails, Order order, Long userId, Integer totalAmount) {

        orderDetails.stream().forEach((orderDetail) -> {

            InMemorySpinLock inMemorySpinLock = new InMemorySpinLock();
            inMemorySpinLock.acquireLock(orderDetail.getProduct().getId());

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
                inMemorySpinLock.releaseLock(orderDetail.getProduct().getId());
                log.info("stock : " + productChangingAmount.getStock());
            } else {

                //TODO : 결제 취소 로직 추가
                paymentService.cancelByBusiness(userId, totalAmount);
                throw new OrderException(OrderErrorCode.OUT_OF_STOCK);
            }
        });


    }

    // 인메모리 스핀락

    public static class InMemorySpinLock {

        private final ConcurrentHashMap<Long, Lock> stockLockMap = new ConcurrentHashMap<>();
        public String acquireLock(Long resourceKey) {
            Lock lock = stockLockMap.computeIfAbsent(resourceKey, key -> new ReentrantLock());
            while (true) {
                if (lock.tryLock()) return "Locked";
                try {
                    TimeUnit.MICROSECONDS.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        public void releaseLock(Long resourceKey) {
            Lock lock = stockLockMap.get(resourceKey);
            if (lock != null) {
                lock.unlock();
            }
        }
    }


}
