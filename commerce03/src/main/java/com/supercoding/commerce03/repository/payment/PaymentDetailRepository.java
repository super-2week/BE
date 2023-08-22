package com.supercoding.commerce03.repository.payment;

import com.supercoding.commerce03.repository.payment.entity.PaymentDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Long> {
    Page<PaymentDetail> findAllByPaymentId(Long paymentId, Pageable pageable);

}
