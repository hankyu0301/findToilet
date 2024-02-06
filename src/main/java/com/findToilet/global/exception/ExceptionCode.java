package com.findToilet.global.exception;

import lombok.Getter;

public enum ExceptionCode {
    TEST_CODE("테스트", 200),
    LOG_NOT_FOUND("로그 파일이 존재하지 않습니다.", 404),
    INTERNAL_SERVER_ERROR("Internal Server Error", 500),
    NOT_IMPLEMENTATION("Not Implementation", 501),

    //  인증
    SIGNATURE_INVALID("잘못된 JWT 서명입니다.", 401),
    TOKEN_FORMAT_INVALID("올바르지 않는 토큰입니다.", 401),
    AUTH_MAIL_CODE_MISMATCH("인증코드가 올바르지 않습니다.", 403),
    AUTH_MAIL_CODE_NOT_FOUND("발급받은 인증코드를 찾을 수 없습니다. 인증코드를 다시 받아주세요.", 404),
    REFRESH_TOKEN_NOT_FOUND("토큰을 찾을 수 없거나 만료됐습니다. 다시 로그인해주세요.", 401),
    ACCESS_TOKEN_NOT_FOUND("엑세스 토큰을 찾을 수 없습니다.", 401),
    NON_REGISTERED_USER("회원가입하지 않은 이메일입니다.", 400),
    PASSWORD_INVALID("비밀번호가 맞지 않습니다.", 400),
    UNKNOWN_USER("알 수 없는 사용자입니다.", 400),
    LOGOUT_USER("로그아웃된 유저입니다.", 401),
    FORBIDDEN("권한이 없습니다.", 403),

    // 회원
    MEMBER_NOT_FOUND("해당 회원을 찾을 수 없습니다.", 404),
    EMAIL_EXISTS("이미 존재하는 이메일입니다.", 409),

    // 화장실
    TOILET_NOT_FOUND("해당 화장실을 찾을 수 없습니다.", 404),

    // 리뷰
    REVIEW_NOT_FOUND("해당 리뷰를 찾을 수 없습니다.", 404),

    // API
    JSON_FORMAT_PARSING("잘못된 JSON 포맷 입니다.", 400);

    @Getter
    private final String message;

    @Getter
    private final int statusCode;

    ExceptionCode(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
