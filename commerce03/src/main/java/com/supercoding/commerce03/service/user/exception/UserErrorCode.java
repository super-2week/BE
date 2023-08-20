package com.supercoding.commerce03.service.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode {
    //status(HttpStatus.badRequest) 400
    INVALID_SIGNUP_FILED("기입한 정보를 다시 확인해주세요.", HttpStatus.BAD_REQUEST),
    EMAIL_DUPLICATION( "이미 등록된 이메일입니다.",HttpStatus.BAD_REQUEST),
    INVALID_PHONE_NUMBER_PATTERN("비밀번호는 특수문자를 1개이상 포함해야 합니다.",HttpStatus.BAD_REQUEST),
    INVALID_PHONE_NUMBER("비밀번호는 8자리 이상이여야 합니다.",HttpStatus.BAD_REQUEST),
    NOT_AUTHORIZED("권한이 없습니다(본인만 삭제(조회)할 수 있습니다)",HttpStatus.BAD_REQUEST),
    MISMATCH_PASSWORD("비밀번호를 똑같이 입력 해주세요.",HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD("올바르지 않은 비밀번호 양식 입니다.",HttpStatus.BAD_REQUEST),
    INVALID_LOGIN_INPUT("해당 회원은 없습니다 다시 한번 입력 해줏세요 ",HttpStatus.BAD_REQUEST),
    //status(HttpStatus.NOT_FOUND) 404
    USER_NOT_FOUND("존재하지 않는 유저 입니다.", HttpStatus.NOT_FOUND);

    private final String description;
    private final HttpStatus httpStatus;
}

