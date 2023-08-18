package com.supercoding.commerce03.repository.payment.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Payments_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDetail {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_detail_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(name = "created_at") // 페이충전일
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "business_type") // 충전/사용
    @Enumerated(EnumType.STRING)
    private BusinessByType businessType;

    @Column(name = "total_pay_coin", nullable = false) // 페이 총 보유액
    private Integer totalPayCoin;

    @Column(name = "pay_coin", nullable = false) // 충전한 페이액
    private Integer payCoin;
}

