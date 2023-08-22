package com.supercoding.commerce03.web.dto.product.util;

import lombok.Getter;

@Getter
public enum AnimalCategory {
    DOG(1, "dog"),
    CAT(2, "cat"),
    SMALL(3, "small");

    private final int code;
    private final String name;

    AnimalCategory(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getByCode(int code) {
        switch (code) {
            case 1:
                return DOG.name;
            case 2:
                return CAT.name;
            case 3:
                return SMALL.name;
            default:
                return DOG.name;
        }
    }
}