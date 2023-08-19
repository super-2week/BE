package com.supercoding.commerce03.web.dto.product.util;

public enum ProductCategory {
    FOOD(1, "food"),
    SNACK(2, "snack"),
    CLEAN(3, "clean"),
    TABLEWARE(4, "tableware"),
    HOUSE(5, "house"),
    CLOTH(6, "cloth"),
    ETC(7, "etc");

    private final int code;
    private final String name;

    ProductCategory(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getByCode(int code) {
        switch(code){
            case 1: return FOOD.name;
            case 2: return SNACK.name;
            case 3: return CLEAN.name;
            case 4: return TABLEWARE.name;
            case 5: return HOUSE.name;
            case 6: return CLOTH.name;
            case 7: return ETC.name;
            default: return FOOD.name;
        }
    }
}
