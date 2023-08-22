package com.supercoding.commerce03.web.controller.order;

import com.supercoding.commerce03.service.order.OrderService;
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
    @PostMapping("")
    public ResponseEntity<?> orderRegister(
            @RequestBody OrderDto.OrderRegisterRequest orderRegisterRequest
    ) {
        //TODO : 임시 유저 정보 실제 정보 받아와서 바꿔야함.
        String userId = "1";
        OrderDto.OrderResponse orderResponse
                = orderService.orderRegister(userId, orderRegisterRequest);
        return ResponseEntity.ok(orderResponse);
    }
    //주문 취소
//    @Auth
    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> orderCancel(
            @PathVariable String orderId
    ) {
        //TODO : 임시 유저 정보 실제 정보 받아와서 바꿔야함.
        //        Long userId = AuthHolder.getUserId();
        String userId = "1";
        OrderDto.OrderCancelResponse orderCancelResponse
                = orderService.orderCancel(userId, orderId);

        return ResponseEntity.ok(orderCancelResponse);
    }

    //주문 목록 가져오기 페이지네이션 처리
    @GetMapping("/list")
    public ResponseEntity<?> orderList(
            Pageable pageable
    ){
        //TODO : 임시 유저 정보 실제 정보 받아와서 바꿔야함.
        String userId = "2";

        Page<OrderDto.OrderListResponse> orderListResponsePage
                = orderService.orderList(userId,pageable);

        return ResponseEntity.ok(orderListResponsePage);
    }

    //주문 목록에서 주문 내역 삭제 하기
    @DeleteMapping("/list/{orderId}")
    public ResponseEntity<?> deleteOneInOrderList(
            @PathVariable String orderId
    ) {
        String response = orderService.deleteOneInOrderList(orderId);
        return ResponseEntity.ok(response);

    }

    //주문 상세 보기
    @GetMapping("/detail/{orderId}")
    public ResponseEntity<?> orderViewDetail(
            @PathVariable String orderId
    ) {
        OrderDto.OrderResponse orderDetailResponse
                = orderService.orderViewDetail(orderId);

        return ResponseEntity.ok(orderDetailResponse);

    }

}
