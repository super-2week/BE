package com.supercoding.commerce03.repository.product.entity;

import com.supercoding.commerce03.repository.store.entity.Store;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	@Column(name = "image_url", length = 50) //null 허용
	private String imageUrl;

	@Column(name = "animal_category", nullable = false) //1~4
	private Integer animalCategory;

	@Column(name = "product_category", nullable = false) //1~6
	private Integer productCategory;

	@Column(name = "product_name", nullable = false, length = 20)
	private String productName;

	@Column(name = "model_num", nullable = true, length = 20)
	private String modelNum;

	@Column(name = "origin_label", nullable = false, length = 20, columnDefinition = "varchar(20) default '한국'")
	private String originLabel;

	@Column(name = "price", nullable = false)
	private Integer price;

	@Column(name = "description", nullable = false, length = 100)
	private String description;

	@Column(name = "stock", nullable = false, columnDefinition = "int default 0")
	private Integer stock;

	@Column(name = "wish_count", nullable = false, columnDefinition = "int default 0")
	private Integer wishCount;

	@Column(name = "purchase_count", nullable = false, columnDefinition = "int default 0")
	private Integer purchaseCount;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;



}
