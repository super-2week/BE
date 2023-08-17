package com.supercoding.commerce03.repository.payment;

import com.supercoding.commerce03.repository.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {


    Optional<Payment> findByUserId(Long userId);

}
