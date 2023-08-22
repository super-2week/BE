package com.supercoding.commerce03.web.dto.product;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class GetRequestDto {
    private String animalCategory;
    private String productCategory;
    private String sortBy;
    private String searchWord;

    public GetRequestDto(String animalCategory, String productCategory, String sortBy) {
        this.animalCategory = animalCategory;
        this.productCategory = productCategory;
        this.sortBy = sortByConvert(sortBy);
    }


    public GetRequestDto(String animalCategory) {
        this.animalCategory = animalCategory;
    }

    public GetRequestDto(String animalCategory, String productCategory) {
        this.animalCategory = animalCategory;
        this.productCategory = productCategory;
    }




    public String sortByConvert(String sortBy) {
        if (sortBy == null) {
            return "price";
        }

        switch (sortBy) {
            case "price":
                return "price";
            case "createdAt":
                return "createdAt";
            case "popular":
                return "wishCount";
            default:
                return "price"; // 또는 다른 기본값 또는 에러 처리 로직 추가
        }
    }
}


