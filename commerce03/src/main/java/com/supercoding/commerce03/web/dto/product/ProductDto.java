package com.supercoding.commerce03.web.dto.product;

import com.supercoding.commerce03.repository.store.entity.Store;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    private String imageUrl;
    private Integer animalCategory; //DB접근용도
    private Integer productCategory; //DB접근용도
    private String storeName;
    private String productName;
    private String modelNum;
    private String originLabel;
    private Integer price;
    private String description;
    private Integer stock;
    private Integer wishCount;
    private Integer purchaseCount;
    private LocalDateTime createdAt;
    private boolean isLiked;
    private Store store;

    //DB 접근용 생성자
    public ProductDto(Long id, String imageUrl, Integer animalCategory, Integer productCategory, String productName, String modelNum, String originLabel, Integer price, String description, Integer stock, Integer wishCount, Integer purchaseCount, LocalDateTime createdAt, Store store) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.animalCategory = animalCategory;
        this.productCategory = productCategory;
        this.productName = productName;
        this.modelNum = modelNum;
        this.originLabel = originLabel;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.wishCount = wishCount;
        this.purchaseCount = purchaseCount;
        this.createdAt = createdAt;
        this.store = store;
    }

}
