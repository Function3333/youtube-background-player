package com.rest.ybp.common;

public class Response {

    private String result;
    private Object data;


    public Response(String result, Object data) {
        this.result = result;
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public Object getData() {
        return data;
    }
}
