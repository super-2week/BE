package com.supercoding.commerce03.repository.payment;

import com.supercoding.commerce03.repository.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
