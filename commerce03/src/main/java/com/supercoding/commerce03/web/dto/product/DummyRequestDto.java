package com.supercoding.commerce03.web.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DummyRequestDto {
    private int storeId;
    private String imageUrl;
    private int animalCategory;
    private int productCategory;
    private String productName;
    private int price;
    private String description;
    private int stock;
    private int wishCount;

    public DummyRequestDto(JSONObject dummyJson) {
        this.imageUrl = dummyJson.getString("imageUrl");
        this.animalCategory = dummyJson.getInt("animalCategory");
        this.productCategory = dummyJson.getInt("productCategory");
        this.productName = dummyJson.getString("productName");
        this.price = dummyJson.getInt("price");
        this.description = dummyJson.getString("description");
        this.stock = dummyJson.getInt("stock");
        this.wishCount = dummyJson.getInt("wishCount");
    }

}
