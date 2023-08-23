package com.supercoding.commerce03.service.payment;

import com.supercoding.commerce03.repository.payment.PaymentDetailRepository;
import com.supercoding.commerce03.repository.payment.PaymentRepository;
import com.supercoding.commerce03.repository.payment.entity.BusinessByType;
import com.supercoding.commerce03.repository.payment.entity.Payment;
import com.supercoding.commerce03.repository.payment.entity.PaymentDetail;
import com.supercoding.commerce03.repository.user.entity.User;
import com.supercoding.commerce03.service.payment.exception.PaymentErrorCode;
import com.supercoding.commerce03.service.payment.exception.PaymentException;

import com.supercoding.commerce03.web.dto.payment.Charge;
import com.supercoding.commerce03.web.dto.payment.TotalCoin;
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
    private final PaymentDetailRepository paymentDetailRepository;

    public void createPayment(User user){
        Payment payment = Payment.createPayment(user);
        paymentRepository.save(payment);
    }

    @Transactional
    public Charge.Response chargeByCoin(Long userId, Charge.Request request) {
        Payment validatedPayment = validatePayment(userId);
        int chargeTotalCoin = validatedPayment.getTotalCoin() + request.getCoin();
        validatedPayment.setTotalCoin(chargeTotalCoin);
        validatedPayment.setCoin(request.getCoin());
        validatedPayment.setCreatedAt(LocalDateTime.now());
        validatedPayment.setBusinessType(BusinessByType.CHARGE);

        paymentDetailRepository.save(PaymentDetail.builder()
                .payment(validatedPayment)
                .createdAt(LocalDateTime.now())
                .businessType(BusinessByType.CHARGE)
                .totalPayCoin(chargeTotalCoin)
                .payCoin(validatedPayment.getCoin())
                .build());
        return Charge.Response.from(validatedPayment);
    }

    public TotalCoin.Response findByPaymentId(Long userId, Pageable pageable) {
        Payment validatedPayment = validatePayment(userId);
        Page<PaymentDetail> paymentDetails = paymentDetailRepository.findAllByPaymentId(validatedPayment.getId(), pageable);
        List<TotalCoin.Response.PaymentDetail> paymentDetailList = paymentDetails.stream().map(paymentDetail -> TotalCoin.Response.PaymentDetail.builder()
                .totalCoin(paymentDetail.getTotalPayCoin())
                .businessType(paymentDetail.getBusinessType().getKey())
                .coin(paymentDetail.getPayCoin())
                .createdAt(paymentDetail.getCreatedAt())
                .build()).collect(Collectors.toList());
             return TotalCoin.Response.from(validatedPayment, paymentDetailList);
    }

    public void cancelByBusiness(Long userId, Integer totalAmount) {

        Payment validatedPayment = validatePayment(userId);
        int cancelTotalCoin = validatedPayment.getTotalCoin() + totalAmount;
        validatedPayment.setTotalCoin(cancelTotalCoin);
        paymentRepository.save(validatedPayment);


        PaymentDetail paymentDetail = PaymentDetail.builder()
                .payment(validatedPayment)
                .createdAt(LocalDateTime.now())
                .businessType(BusinessByType.CANCEL)
                .totalPayCoin(cancelTotalCoin)
                .payCoin(totalAmount)
                .build();

        paymentDetailRepository.save(paymentDetail);
    }


    public void orderByBusiness(Long userId, Integer totalAmount) {
        Payment validatedPayment = validatePayment(userId);
        if (validatedPayment.getTotalCoin() < totalAmount) {
            throw new PaymentException(PaymentErrorCode.LACK_OF_POINT);
        } else {
            int orderTotalCoin = validatedPayment.getTotalCoin() - totalAmount;
            validatedPayment.setTotalCoin(orderTotalCoin);

            paymentRepository.save(validatedPayment);

            PaymentDetail paymentDetail = PaymentDetail.builder()
                    .payment(validatedPayment)
                    .createdAt(LocalDateTime.now())
                    .businessType(BusinessByType.USE)
                    .totalPayCoin(orderTotalCoin)
                    .payCoin(totalAmount)
                    .build();

            paymentDetailRepository.save(paymentDetail);
        }
    }

    private Payment validatePayment(Long userId){
        return paymentRepository.findByUserId(userId)
                .orElseThrow(() -> new PaymentException(PaymentErrorCode.PAYMENT_NOT_FOUND));
    }
}
