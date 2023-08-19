package com.supercoding.commerce03.web.dto.product;

import com.supercoding.commerce03.web.dto.product.util.AnimalCategory;
import com.supercoding.commerce03.web.dto.product.util.ProductCategory;
import com.supercoding.commerce03.web.dto.product.util.SmallCategory;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String imageUrl;
    private Integer animalCategory;
    private Integer productCategory;
    private String productName;
    private String storeName;
    private String modelNum;
    private String originLabel;
    private Integer price;
    private String description;
    private Integer stock;
    private Integer wishCount;
    private Integer purchaseCount;
    private LocalDateTime createdAt;
    private boolean isLiked;

    public ProductDto(Long id, String imageUrl, Integer animalCategory, Integer productCategory, String productName, String storeName, String modelNum, String originLabel, Integer price, String description, Integer stock, Integer wishCount, Integer purchaseCount, LocalDateTime createdAt) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.animalCategory = animalCategory;
        this.productCategory = productCategory;
        this.productName = productName;
        this.storeName = storeName;
        this.modelNum = modelNum;
        this.originLabel = originLabel;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.wishCount = wishCount;
        this.purchaseCount = purchaseCount;
        this.createdAt = createdAt;
    }
}
