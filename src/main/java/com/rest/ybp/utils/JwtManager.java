package com.rest.ybp.utils;

import com.rest.ybp.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.lang.Maps;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtManager {
    private static final SecretKey secretKey =Jwts.SIG.HS256.key().build();
    public String createJwtToken(User user) {
        return Jwts.builder()
                .signWith(secretKey)
                .claim("user", user)
                .compact();
    }

    public void parseJwtToken(String jwt) {
        System.out.println("parseToken secretKey = " + secretKey.toString());
        Object o = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }
}
