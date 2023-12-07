package com.rest.ybp.common;

public class Response {

    private String status;
    private String msg;

    public Response(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }
}
