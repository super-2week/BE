package com.supercoding.commerce03.repository.account.entity;

import com.supercoding.commerce03.repository.order.entity.Order;
import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.repository.user.entity.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "Accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id", nullable = false)
	private Long id;

	@OneToOne
	@JoinColumn(name="user_id", nullable = false)
	private User user;

	@OneToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@Column(name = "account_by", nullable = false)
	private Integer accountBy;

	@Column(name = "account_num", nullable = false)
	private Integer accountNum;

	@Column(name = "card_num", nullable = false)
	private Integer cardNum;

	@Column(name = "total_account", nullable = false)
	private Integer totalAccount;

	@Column(name = "created_at")
	@CreatedDate
	private String created_at;
}
