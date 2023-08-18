package com.supercoding.commerce03.repository.payment.entity;

import javax.persistence.*;

import com.supercoding.commerce03.repository.user.entity.User;
import com.supercoding.commerce03.repository.user.entity.UserDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "Payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id", nullable = false)
	private User user;

	@Column(name = "created_at") // 페이충전일
	@CreatedDate
	private LocalDateTime createdAt;

	@Column(name = "business_type") // 충전/사용
	@Enumerated(EnumType.STRING)
	private BusinessByType businessType;

	@Column(name = "total_coin", nullable = false) // 페이 총 보유액
	private Integer totalCoin;

	@Column(name = "coin", nullable = false) // 충전한 페이액
	private Integer coin;

	public static Payment createPayment(User user) {
		Payment payment = new Payment();
		payment.setCreatedAt(payment.createdAt);
		payment.setCoin(0);
		payment.setTotalCoin(0);
		return payment;
	}
}
