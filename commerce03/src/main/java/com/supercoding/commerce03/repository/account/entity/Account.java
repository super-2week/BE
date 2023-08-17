//package com.supercoding.commerce03.repository.account.entity;
//
//import com.supercoding.commerce03.repository.order.entity.Order;
//import com.supercoding.commerce03.repository.product.entity.Product;
//import com.supercoding.commerce03.repository.user.entity.User;
//
//import javax.persistence.*;
//
//import lombok.*;
//import org.springframework.data.annotation.CreatedDate;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "Accounts")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@EqualsAndHashCode(of="account_id")
//public class Account {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "account_id", nullable = false)
//	private Long id;
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name="user_id", nullable = false)
//	private User user;
//
//	@Column(name = "account_by", nullable = false) // 페이충전수단
//	@Enumerated(EnumType.STRING)
//	private AccountByType accountBy;
//
//	@Column(name = "number") // 계좌번호
//	private Integer num;
//
////	@Column(name = "card_num") // 카드번호
////	private Integer cardNum;
//
//	@Column(name = "total_account", nullable = false) //페이충전액
//	private Integer totalAccount;
//
//
//	@Column(name = "created_at") // 페이충전일
//	@CreatedDate
//	private LocalDateTime createdAt;
//
//
//}
