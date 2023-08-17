package com.supercoding.commerce03.web.controller.order;

import com.supercoding.commerce03.service.order.OrderService;
import com.supercoding.commerce03.web.dto.order.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            ){
        //TODO : 임시 유저 정보 실제 정보 받아와서 바꿔야함.
        String userId = "1";
        OrderDto.OrderRegisterResponse orderResponse
                = orderService.orderRegister(userId,orderRegisterRequest);
        return ResponseEntity.ok(orderResponse);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> orderCancel(
            @PathVariable String orderId
    ) {
        //TODO : 임시 유저 정보 실제 정보 받아와서 바꿔야함.
        String userId = "1";
        OrderDto.OrderCancelResponse orderCancelResponse
                = orderService.orderCancel(userId, orderId);

        return ResponseEntity.ok(orderCancelResponse);

    }

}
