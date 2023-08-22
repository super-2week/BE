package com.supercoding.commerce03.service.security;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthHolder {
    private static final ThreadLocal<Long>userIdHolder = new ThreadLocal<>();
    public static void setUserId(Long userId) {
        userIdHolder.set(userId);
    }
    public static Long getUserId() {
      return  userIdHolder.get();
    }
}
