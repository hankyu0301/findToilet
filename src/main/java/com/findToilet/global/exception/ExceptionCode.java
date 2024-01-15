package com.findToilet.global.exception;

import lombok.Getter;

public enum ExceptionCode {
    TEST_CODE("테스트", 200),
    LOG_NOT_FOUND("로그 파일이 존재하지 않습니다.", 404),
    INTERNAL_SERVER_ERROR("Internal Server Error", 500),
    NOT_IMPLEMENTATION("Not Implementation", 501),

    // 회원
    MEMBER_NOT_FOUND("해당 회원을 찾을 수 없습니다.", 404),
    EMAIL_EXISTS("이미 존재하는 이메일입니다.", 409);

    @Getter
    private final String message;

    @Getter
    private final int statusCode;

    ExceptionCode(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
