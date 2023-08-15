package com.supercoding.commerce03.repository.payment.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

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

	@Column(name = "created_at")
	@CreatedDate
	private String created_at;

	@Column(name = "total_payment", nullable = false)
	private Integer totalPayment;

	@Column(name = "order_id")
	private Integer orderId;

	@Column(name = "user_id")
	private Integer userId;

}
