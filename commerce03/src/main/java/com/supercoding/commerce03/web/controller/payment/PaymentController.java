package com.supercoding.commerce03.web.controller.payment;

import com.supercoding.commerce03.service.payment.PaymentService;
import com.supercoding.commerce03.web.dto.payment.Charge;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PatchMapping("/charge")
    public Charge.Response charge(@RequestBody Charge.Request request){
        Long userId = 1L;
        return paymentService.chargeByCoin(userId,request);
    }

    @GetMapping
    public List<Charge.Response> findByPaymentId() {
        Long userId = 1L;
        return paymentService.findByPaymentId(userId);
    }

}
