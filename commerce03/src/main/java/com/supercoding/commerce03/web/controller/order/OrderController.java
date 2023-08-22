package com.supercoding.commerce03.web.controller.order;

import com.supercoding.commerce03.service.order.OrderService;
import com.supercoding.commerce03.service.security.Auth;
import com.supercoding.commerce03.service.security.AuthHolder;
import com.supercoding.commerce03.web.dto.order.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/order")

@Slf4j
public class OrderController {
    private final OrderService orderService;

    //주문하기(결제)
    @Auth
    @PostMapping("")
    public ResponseEntity<?> orderRegister(
            @RequestBody OrderDto.OrderRegisterRequest orderRegisterRequest
    ) {
        Long userId = AuthHolder.getUserId();
        OrderDto.OrderResponse orderResponse
                = orderService.orderRegister(userId, orderRegisterRequest);
        return ResponseEntity.ok(orderResponse);
    }

    //주문 취소
    @Auth
    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> orderCancel(
            @PathVariable String orderId
    ) {
        Long userId = AuthHolder.getUserId();
        OrderDto.OrderCancelResponse orderCancelResponse
                = orderService.orderCancel(userId, orderId);

        return ResponseEntity.ok(orderCancelResponse);
    }

    //주문 목록 가져오기 페이지네이션 처리
    @Auth
    @GetMapping("/list")
    public ResponseEntity<?> orderList(
            Pageable pageable
    ) {
        Long userId = AuthHolder.getUserId();

        Page<OrderDto.OrderListResponse> orderListResponsePage
                = orderService.orderList(userId, pageable);

        return ResponseEntity.ok(orderListResponsePage);
    }

    //주문 목록에서 주문 내역 삭제 하기
    @Auth
    @DeleteMapping("/list/{orderId}")
    public ResponseEntity<?> deleteOneInOrderList(
            @PathVariable String orderId
    ) {
        Long userId = AuthHolder.getUserId();
        String response = orderService.deleteOneInOrderList(userId,orderId);
        return ResponseEntity.ok(response);

    }

    //주문 상세 보기
    @Auth
    @GetMapping("/detail/{orderId}")
    public ResponseEntity<?> orderViewDetail(
            @PathVariable String orderId
    ) {
        Long userId = AuthHolder.getUserId();
        OrderDto.OrderResponse orderDetailResponse
                = orderService.orderViewDetail(userId,orderId);

        return ResponseEntity.ok(orderDetailResponse);

    }

}
