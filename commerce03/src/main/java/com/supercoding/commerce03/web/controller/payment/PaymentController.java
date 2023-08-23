package com.supercoding.commerce03.web.controller.payment;

import com.supercoding.commerce03.service.payment.PaymentService;
import com.supercoding.commerce03.service.security.Auth;
import com.supercoding.commerce03.service.security.AuthHolder;
import com.supercoding.commerce03.web.dto.payment.Charge;

import com.supercoding.commerce03.web.dto.payment.TotalCoin;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Auth
    @PatchMapping("/charge")
    public ResponseEntity<Charge.Response> charge(@RequestBody Charge.Request request){
        Long userId = AuthHolder.getUserId();
        return ResponseEntity.ok(paymentService.chargeByCoin(userId,request));
    }

    @Auth
    @GetMapping
    public ResponseEntity<TotalCoin.Response> findByPaymentId(Pageable pageable) {
        Long userId = AuthHolder.getUserId();

        return ResponseEntity.ok(paymentService.findByPaymentId(userId, pageable));
    }
}
