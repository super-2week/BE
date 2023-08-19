package com.supercoding.commerce03.web.dto.product.util;

public enum SmallCategory {
    FOOD(1, "food"),
    EQUIPMENT(2, "equipment"),
    HOUSE(3, "house");

    private final int code;
    private final String name;

    SmallCategory(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getByCode(int code) {
        switch(code){
            case 1: return FOOD.name;
            case 2: return EQUIPMENT.name;
            case 3: return HOUSE.name;
            default: return FOOD.name;
        }
    }
}