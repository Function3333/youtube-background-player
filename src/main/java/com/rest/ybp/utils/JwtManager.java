package com.rest.ybp.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.ybp.user.User;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Calendar;
import java.util.Date;

@Component
public class JwtManager {
    private static final SecretKey secretKey = Jwts.SIG.HS256.key().build();
    private static final int ACCESS_TOKEN_VALIDATE_TIME = 30; //Minute
    private static final int REFRESH_TOKEN_VALIDATE_TIME = 3; //Day

    public String getAccessToken(String userName) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        return Jwts.builder()
                .signWith(secretKey)
                .subject("AccessToken")
                .expiration(getAccessTokenValidateTime())
                .claim("user", mapper.writeValueAsString(userName))
                .compact();
    }

    public String getRefreshToken(String userName) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        return Jwts.builder()
                .signWith(secretKey)
                .subject("AccessToken")
                .expiration(getRefreshTokenValidateTime())
                .claim("user", mapper.writeValueAsString(userName))
                .compact();
    }

    public User parseJwtToken(String jwt) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            Object o = Jwts
                    .parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload()
                    .get("user");
            System.out.println(o.toString());
            return mapper.readValue(o.toString(), User.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Date getAccessTokenValidateTime() {
        Date now = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        calendar.add(Calendar.MINUTE, ACCESS_TOKEN_VALIDATE_TIME);
        return calendar.getTime();
    }

    public Date getRefreshTokenValidateTime() {
        Date now = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        calendar.add(Calendar.DAY_OF_WEEK, REFRESH_TOKEN_VALIDATE_TIME);
        return calendar.getTime();
    }
}
