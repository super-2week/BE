package com.supercoding.commerce03.repository.order.entity;


import com.supercoding.commerce03.repository.product.entity.Product;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="Order_details")
@EqualsAndHashCode(of="order_detail_id")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_detail_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name="product_id", nullable = false)
    private Product product;

    @Column(name="price", nullable = false)
    private Integer price;

    @Column(name = "amount", nullable = false, length= 100)
    private Integer amount;

    @Column(name="is_deleted", nullable = false)
    private Boolean isDeleted;
}
