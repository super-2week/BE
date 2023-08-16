package com.supercoding.commerce03.service.user;

import com.supercoding.commerce03.repository.payment.PaymentRepository;
import com.supercoding.commerce03.repository.payment.entity.Payment;
import com.supercoding.commerce03.repository.user.UserRepository;
import com.supercoding.commerce03.repository.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    // 유저 id로 User 찾기
    public User findUser(Integer id) {
        return userRepository.findById(Long.valueOf(id)).get();
    }

    public void chargePoint(int id,int amount){
        Payment payment = paymentRepository.findById(id);
        payment.setCoin(payment.getCoin() + amount);

        paymentRepository.save(payment);
    }
}
