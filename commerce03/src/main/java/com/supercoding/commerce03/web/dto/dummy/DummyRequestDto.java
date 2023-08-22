package com.supercoding.commerce03.web.dto.dummy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DummyRequestDto {
    private int storeId;
    private String imageUrl;
    private int animalCategory;
    private int productCategory;
    private String productName;
    private String modelNum;
    private String originLabel;
    private int price;
    private String description;
    private int stock;
    private int wishCount;
    private int purchaseCount;

    public DummyRequestDto(JSONObject dummyJson) {
        this.storeId = dummyJson.getInt("storeId");
        this.imageUrl = dummyJson.getString("imageUrl");
        this.animalCategory = dummyJson.getInt("animalCategory");
        this.productCategory = dummyJson.getInt("productCategory");
        this.productName = dummyJson.getString("productName");
        this.modelNum = dummyJson.getString("modelNum");
        this.originLabel = dummyJson.getString("originLabel");
        this.price = dummyJson.getInt("price");
        this.description = dummyJson.getString("description");
        this.stock = dummyJson.getInt("stock");
        this.wishCount = dummyJson.getInt("wishCount");
        this.purchaseCount = dummyJson.getInt("purchaseCount");
    }


    public static List<DummyRequestDto> fromJsonArray(JSONObject jsonObject) {
        JSONArray jsonArray  = jsonObject.getJSONArray("products");
        List<DummyRequestDto> dtoList = new ArrayList<>();

        for (Object o : jsonArray) {
            DummyRequestDto dto = new DummyRequestDto((JSONObject) o);
            dtoList.add(dto);
        }

//        for (int i = 0; i < jsonArray.length(); i++) {
//            JSONObject jsonObject = jsonArray.getJSONObject(i);
//            DummyRequestDto dto = new DummyRequestDto(jsonObject);
//            dtoList.add(dto);
//        }

        return dtoList;
    }

}
