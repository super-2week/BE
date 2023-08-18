package com.supercoding.commerce03.web.dto.dummy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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


    public static List<DummyStoreDto> createFromJsonArray(JSONArray jsonArray) {
            List<DummyStoreDto> dtos = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                DummyStoreDto dto = new DummyStoreDto(jsonObject);
                dtos.add(dto);
            }

            return dtos;
        }


}
