package com.rest.ybp.common;

import java.util.Date;

public class Token {
    private String payload;
    private String expiredDate;

    public Token(String payload, Date expiredDate) {
        this.payload = payload;
        this.expiredDate = expiredDate.toString();
    }

    public String getPayload() {
        return this.payload;
    }

    public String getExpiredDate() {
        return this.expiredDate;
    }
}
