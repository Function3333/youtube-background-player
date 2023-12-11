package com.rest.ybp.common;

public enum Result {
    EXTRACT_URL_FAIL("유효하지 않은 URL입니다."),
    EXTRACT_AUDIO_FAIL("음원 추출에 실패했습니다."),
    UPLOAD_AUDIO_FAIL("음원 업로드에 실패했습니다."),
    SEND_MAIL_FAIL("메일 전송에 실피했습니다."),
    DUPLICATE_NAME("중복된 아이디입니다."),
    DUPLICATE_EMAIL("중복된 이메일입니다."),
    VERIFY_EMAIL_FAIL("이메일 인증에 실패했습니다."),
    SUCCESS("성공하였습니다.");

    private final String msg;

    Result(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }
}
