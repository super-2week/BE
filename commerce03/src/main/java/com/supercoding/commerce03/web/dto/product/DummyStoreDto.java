package com.supercoding.commerce03.web.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DummyStoreDto {
    private String storeName;
    private String contact;

    public DummyStoreDto(JSONObject dummyJson) {
        this.storeName = dummyJson.getString("storeName");
        this.contact = dummyJson.getString("contact");

    }
}
