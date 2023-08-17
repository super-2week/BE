package com.supercoding.commerce03.repository.payment.entity;

import javax.persistence.*;

import com.supercoding.commerce03.repository.user.entity.UserDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

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
	private Integer id;

	@OneToOne
	@JoinColumn(name = "user_detail_id", nullable = false)
	private UserDetail userDetail;

	@Column(name = "created_at") // 페이충전일
	@CreatedDate
	private LocalDateTime createdAt;

	@Column(name = "total_payment", nullable = false) // 페이보유액
	private Integer totalPayment;

}
