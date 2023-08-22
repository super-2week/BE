package com.supercoding.commerce03.repository.payment.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BusinessByType {

    CHARGE("충전"),
    USE("사용"),
    CREATE("생성"),
    CANCEL("취소");

    private final String key;
}


