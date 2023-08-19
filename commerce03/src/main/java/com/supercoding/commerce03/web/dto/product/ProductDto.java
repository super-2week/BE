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
    private String animalCategory;
    private String productCategory;
    private String productName;
    private Integer price;
    private String description;
    private Integer stock;
    private Integer wishCount;
    private Integer purchaseCount;
    private LocalDateTime createdAt;

    public ProductDto(Long id, String imageUrl, Integer animalCategory, Integer productCategory, String productName, Integer price, String description, Integer stock, Integer wishCount, Integer purchaseCount, LocalDateTime createdAt) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.animalCategory = AnimalCategory.getByCode(animalCategory);
        this.productCategory = animalCategory == 3 ?
                SmallCategory.getByCode(productCategory) :
                ProductCategory.getByCode(productCategory);
        this.productName = productName;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.wishCount = wishCount;
        this.purchaseCount = purchaseCount;
        this.createdAt = createdAt;
    }
}
