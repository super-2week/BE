package com.supercoding.commerce03.service.payment;

import com.supercoding.commerce03.repository.payment.PaymentDetailRepository;
import com.supercoding.commerce03.repository.payment.PaymentRepository;
import com.supercoding.commerce03.repository.payment.entity.Payment;
import com.supercoding.commerce03.repository.payment.entity.PaymentDetail;
import com.supercoding.commerce03.repository.user.UserRepository;
import com.supercoding.commerce03.repository.user.entity.User;
import com.supercoding.commerce03.service.cart.exception.CartErrorCode;
import com.supercoding.commerce03.service.cart.exception.CartException;
import com.supercoding.commerce03.service.order.exception.OrderErrorCode;
import com.supercoding.commerce03.service.order.exception.OrderException;
import com.supercoding.commerce03.service.payment.exception.PaymentErrorCode;
import com.supercoding.commerce03.service.payment.exception.PaymentException;
import com.supercoding.commerce03.web.dto.payment.Cancel;
import com.supercoding.commerce03.web.dto.payment.Charge;
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
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final PaymentDetailRepository paymentDetailRepository;

    public void createPayment(User user){
        Payment payment = Payment.createPayment(user);
        paymentRepository.save(payment);
    }

    @Transactional
    public Charge.Response chargeByCoin(Long userId, Charge.Request request) {
        Payment validatedUser = validateUser(userId);
        int chargeTotalCoin = validatedUser.getTotalCoin() + request.getCoin();
        validatedUser.setTotalCoin(chargeTotalCoin);
        paymentDetailRepository.save(PaymentDetail.builder()
                .payment(validatedUser)
                .createdAt(LocalDateTime.now())
                .businessType(validatedUser.getBusinessType())
                .totalPayCoin(validatedUser.getTotalCoin())
                .payCoin(validatedUser.getCoin())
                .build());
        return Charge.Response.from(validatedUser);
    }

//    public void chargePoint(Long userId,int amount){
//        Payment payment = paymentRepository.findByUserId(userId);
//        payment.setCoin(payment.getTotalCoin() + amount);
//        userRepository.save(payment);
//    }

    public Page<Charge.Response> findByPaymentId(Long userId, Pageable pageable) {
        Payment validatedUser = validateUser(userId);
        Page<PaymentDetail> paymentDetails = paymentDetailRepository.findAllByPaymentId(validatedUser.getId(), pageable);
//        return paymentDetails.stream()
//                .map(paymentDetail -> Charge.Response.builder()
//                        .businessType(paymentDetail.getBusinessType())
//                        .createdAt(LocalDateTime.now())
//                        .coin(paymentDetail.getPayCoin())
//                        .totalCoin(paymentDetail.getTotalPayCoin())
//                        .build())
//                .collect(Collectors.toList());
//        return paymentDetails.stream().map(Charge.Response::from).collect(Collectors.toList())
            return paymentDetails.map(Charge.Response::from);
    }


    public void cancelByBusiness(Long userId, Integer totalAmount) {
        Payment validatedUser = validateUser(userId);
        int chargeTotalCoin = validatedUser.getTotalCoin() + totalAmount;
        validatedUser.setTotalCoin(chargeTotalCoin);
        paymentRepository.save(validatedUser);
    }


    public void orderByBusiness(Long userId, Integer totalAmount) {
//        log.info("userId : " + userId);
        Payment validatedUser = validateUser(userId);
        if (validatedUser.getTotalCoin() < totalAmount) {
            throw new PaymentException(PaymentErrorCode.LACK_OF_POINT);
        } else {
            validatedUser.setTotalCoin(validatedUser.getTotalCoin() - totalAmount);
        }
        paymentRepository.save(validatedUser);
    }

    private Payment validateUser(Long userId){
        return paymentRepository.findByUserId(userId)
                .orElseThrow(() -> new PaymentException(PaymentErrorCode.PAYMENT_NOT_FOUND));
    }

}
