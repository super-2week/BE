package com.supercoding.commerce03.web.dto.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BannerRequestDto {
    private Integer animalCategory;
    private Integer[] productCategory;

    public BannerRequestDto(String animalCategory) {
        this.animalCategory = animalCategoryConvert(animalCategory);
        this.productCategory = bannerProductCategoryConvert(animalCategory);
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
            default:
                return 1; // 또는 다른 기본값 또는 에러 처리 로직 추가
        }
    }

    private Integer[] bannerProductCategoryConvert(String animalCategory){
        switch(animalCategory){
            case "dog":
                return new Integer[]{1, 2, 3, 4, 5, 6};
            case "cat":
                return new Integer[]{1, 2, 3, 4, 5, 6};
            case "small":
                return new Integer[]{1, 2, 3};
            default:
                return new Integer[]{1, 2, 3, 4, 5, 6};
        }
    }
}
