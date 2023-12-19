package com.rest.ybp.common;

public class Response {

    private String result;
    private String data;


    public Response(String result, String data) {
        this.result = result;
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public String getData() {
        return data;
    }
}
