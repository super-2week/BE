package com.supercoding.commerce03.repository.product.entity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="Products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of="product_id")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Long id;

    @Column(name="image_url", length = 50) //null 허용
    private String imageUrl;

    @Column(name="category", nullable = false)
    private Integer category;

    @Column(name="product_name", nullable = false, length = 20)
    private String productName;

    @Column(name="price", nullable = false)
    private Integer price;

    @Column(name="description", nullable = false, length = 100)
    private String description;

    @Column(name="stock", nullable = false)
    private Integer stock;

    @Column(name="created_at", nullable = false)
    private LocalDateTime created_at;

}
