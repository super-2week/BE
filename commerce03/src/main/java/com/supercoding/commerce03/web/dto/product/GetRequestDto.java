package com.supercoding.commerce03.web.dto.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetRequestDto {
    private Integer animalCategory;
    private Integer productCategory;
    private String sortBy;
    private String searchWord;

    public GetRequestDto(String animalCategory, String productCategory, String sortBy) {
        this.animalCategory = animalCategoryConvert(animalCategory);
        this.productCategory = productCategoryConvert(productCategory);
        this.sortBy = sortByConvert(sortBy);
    }

    public GetRequestDto(String animalCategory) {
        this.animalCategory = animalCategoryConvert(animalCategory);
    }

    public GetRequestDto(String animalCategory, String productCategory) {
        this.animalCategory = animalCategoryConvert(animalCategory);
        this.productCategory = productCategoryConvert(productCategory);
    }

    public Integer animalCategoryConvert(String animalCategory) {
        if (animalCategory == null) {
            return 1; // 기본값 설정 또는 다른 로직 추가
        }

        switch (animalCategory) {
            case "dog":
                return 1;
            case "cat":
                return 2;
            case "small":
                return 3;
            case "etc":
                return 4;
            default:
                return 1; // 또는 다른 기본값 또는 에러 처리 로직 추가
        }
    }

    public Integer productCategoryConvert(String productCategory) {
        if (productCategory == null) {
            return 1; // 기본값 설정 또는 다른 로직 추가
        }

        switch (productCategory) {
            case "food":
                return 1;
            case "snack":
                return 2;
            case "clean":
                return 3;
            case "bowl":
                return 4;
            case "house":
                return 5;
            case "cloth":
                return 6;
            default:
                return 1; // 또는 다른 기본값 또는 에러 처리 로직 추가
        }

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


