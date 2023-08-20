package com.supercoding.commerce03.service.user.exception;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserErrorResponse {
    private UserErrorCode errorCode;
    private String errorMessage;
}
