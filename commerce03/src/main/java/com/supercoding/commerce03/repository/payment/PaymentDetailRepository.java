package com.supercoding.commerce03.repository.payment;

import com.supercoding.commerce03.repository.payment.entity.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Long> {
    List<PaymentDetail> findAllByPaymentId(Long paymentId);

}
