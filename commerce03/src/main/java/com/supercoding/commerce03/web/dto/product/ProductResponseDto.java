package com.supercoding.commerce03.web.dto.product;

import com.supercoding.commerce03.repository.product.entity.Product;
import com.supercoding.commerce03.repository.store.entity.Store;
import com.supercoding.commerce03.web.dto.product.util.AnimalCategory;
import com.supercoding.commerce03.web.dto.product.util.ProductCategory;
import com.supercoding.commerce03.web.dto.product.util.SmallCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ProductResponseDto {
    private Long id;
    private String imageUrl;
    private String animalCategory;
    private String productCategory;
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



    public static ProductResponseDto fromEntity(ProductDto productDto){
        Store store = productDto.getStore();
        String animalCategory = AnimalCategory.getByCode(productDto.getAnimalCategory());
        String productCategory = productDto.getAnimalCategory() == 3 ?
                SmallCategory.getByCode(productDto.getProductCategory()):
                ProductCategory.getByCode(productDto.getProductCategory());

        return ProductResponseDto.builder()
                .id(productDto.getId())
                .animalCategory(animalCategory)
                .productCategory(productCategory)
                .productName(productDto.getProductName())
                .storeName(store.getStoreName())
                .price(productDto.getPrice())
                .imageUrl(productDto.getImageUrl())
                .modelNum(productDto.getModelNum())
                .originLabel(productDto.getOriginLabel())
                .price(productDto.getPrice())
                .description(productDto.getDescription())
                .stock(productDto.getStock())
                .wishCount(productDto.getWishCount())
                .purchaseCount(productDto.getPurchaseCount())
                .createdAt(productDto.getCreatedAt())
                .isLiked(productDto.isLiked())
                .build();
    }

    public static ProductResponseDto fromSearch(Product product){
        return ProductResponseDto.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .build();
    }
}


