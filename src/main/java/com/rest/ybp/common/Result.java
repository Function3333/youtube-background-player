package com.rest.ybp.common;

public enum Result {
    EXTRACT_URL_FAIL("FAIL", "유효하지 않은 URL입니다."),
    EXTRACT_AUDIO_FAIL("FAIL","음원 추출에 실패했습니다."),
    POST_AUDIO_FAIL("FAIL","음원 업로드에 실패했습니다."),
    MAXIMUM_VIDEO_LENGTH_FAIL("FAIL","10분 이하의 영상만 추출 가능합니다."),
    DELETE_AUDIO_FAIL("FAIL","음원 삭제에 실패했습니다."),
    GET_AUDIO_FAIL("FAIL","음원을 가져오는데 실패하였습니다."),
    SEND_MAIL_FAIL("FAIL","메일 전송에 실피했습니다."),
    DUPLICATE_NAME("FAIL","중복된 아이디입니다."),
    DUPLICATE_EMAIL("FAIL","중복된 이메일입니다."),
    VERIFY_EMAIL_FAIL("FAIL","이메일 인증에 실패했습니다."),
    SIGNUP_FAIL("FAIL","회원가입에 실패했습니다"),
    GENERATE_TOKEN_FAIL("FAIL","토근 발급에 실패하였습니다."),
    PARSE_TOKEN_FAIL("FAIL","유효하지 않은 토큰입니다."),
    EMPTY_TOKEN_FAIL("FAIL","로그인이 필요합니다."),
    LOGIN_FAIL("FAIL","로그인 정보가 올바르지 않습니다."),
    SEARCH_FAIL("FAIL","검색정보를 가져오는데 실패했습니다."),
    EXPIRE_ACCESS_TOKEN_FAIL("FAIL", "토큰 재발급이 필요합니다."),
    EXPIRE_REFRESH_TOKEN_FAIL("FAIL", "로그인 기간이 만료되었습니다."),
    SUCCESS("SUCCESS","성공하였습니다.");

    private final String status;
    private final String msg;

    Result(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    public String getStatus() {
        return this.status;
    }
}
