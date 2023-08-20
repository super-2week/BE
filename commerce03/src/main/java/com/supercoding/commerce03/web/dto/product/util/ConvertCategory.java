package com.supercoding.commerce03.web.dto.product.util;

import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ConvertCategory {

    public Integer convertProductCategory(String animalCategory, String productCategory) {
        if (productCategory == null) {
            return 1; // 기본값 설정 또는 다른 로직 추가
        }

        if(Objects.equals(animalCategory, "small")){
            switch (productCategory) {
                case "food":
                    return 1;
                case "equipment":
                    return 2;
                case "house":
                    return 3;
                case "etc":
                    return 4;
                default:
                    return 1; // 또는 다른 기본값 또는 에러 처리 로직 추가
            }
        }else{
            switch (productCategory) {
                case "food":
                    return 1;
                case "snack":
                    return 2;
                case "clean":
                    return 3;
                case "tableware":
                    return 4;
                case "house":
                    return 5;
                case "cloth":
                    return 6;
                case "etc":
                    return 7;
                default:
                    return 1; // 또는 다른 기본값 또는 에러 처리 로직 추가
            }
        }
    }


    public Integer[] assignProductCategoryList(String animalCategory){
        switch(animalCategory){
            case "dog": return new Integer[]{1,2,3,4,5,6,7};
            case "cat": return new Integer[]{1,2,3,4,5,6,7};
            case "small": return new Integer[]{1,2,3,4};
            default : return new Integer[]{1,2,3,4};
        }
    }

    public Integer convertAnimalCategory(String animalCategory) {
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
            default:
                return 1; // 또는 다른 기본값 또는 에러 처리 로직 추가
        }
    }

    public String convertSortBy(String sortBy){
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

