package com.supercoding.commerce03.service.S3.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageErrorCode {
	EMPTY_FILE("빈 파일입니다."),
	NOT_FOUND_FILE("존재하지 않는 파일입니다."),
	FAILED_UPLOAD("이미지 업로드에 실패했습니다."),
	NOT_IMAGE_EXTENSION("이미지 파일이 아닙니다."),
	INVALID_FORMAT_FILE("잘못된 형식의 파일입니다.");

	private final String description;
}
