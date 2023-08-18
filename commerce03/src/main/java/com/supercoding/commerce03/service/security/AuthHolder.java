package com.supercoding.commerce03.service.security;

public class AuthHolder {
    private static final ThreadLocal<Long>userIdHolder = new ThreadLocal<>();
    public static void setUserId(Long userId) {
        userIdHolder.set(userId);
    }
    public static Long getUserId() {
      return  userIdHolder.get();

    }
}
