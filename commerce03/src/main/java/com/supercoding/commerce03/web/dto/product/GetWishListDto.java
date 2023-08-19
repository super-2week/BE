package com.supercoding.commerce03.web.dto.product;

import com.supercoding.commerce03.repository.product.entity.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GetWishListDto {
    private Long wishId;
    private Long productId;
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

    public GetWishListDto(Long wishId, Product product) {
        this.wishId = wishId;
        this.productId = product.getId();
        this.imageUrl = product.getImageUrl();
        this.animalCategory = AnimalCategory.getByCode(product.getAnimalCategory());
        this.productCategory = product.getAnimalCategory() == 3 ?
                SmallCategory.getByCode(product.getProductCategory()) :
                ProductCategory.getByCode(product.getProductCategory());
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.stock = product.getStock();
        this.wishCount = product.getWishCount();
        this.purchaseCount = product.getPurchaseCount();
        this.createdAt = product.getCreatedAt();
    }
}
