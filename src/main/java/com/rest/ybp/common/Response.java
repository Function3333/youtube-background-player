package com.rest.ybp.common;

public class Response {

    private Result status;
    private String msg;

    public Response(Result status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
