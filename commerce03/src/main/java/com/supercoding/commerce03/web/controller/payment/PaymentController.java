package com.supercoding.commerce03.web.controller.payment;

import com.supercoding.commerce03.repository.payment.entity.Payment;
import com.supercoding.commerce03.repository.payment.entity.PaymentDetail;
import com.supercoding.commerce03.service.payment.PaymentService;
import com.supercoding.commerce03.web.dto.payment.Charge;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PatchMapping("/charge")
    public ResponseEntity<Charge.Response> charge(@RequestBody Charge.Request request){
        Long userId = 1L;
        return ResponseEntity.ok(paymentService.chargeByCoin(userId,request));
    }

    @GetMapping
    public ResponseEntity<Page<Charge.Response>> findByPaymentId(Pageable pageable) {
        Long userId = 1L;

        return ResponseEntity.ok(paymentService.findByPaymentId(userId, pageable));
    }
}
