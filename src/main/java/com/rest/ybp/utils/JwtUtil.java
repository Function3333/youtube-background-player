package com.rest.ybp.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;

import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Calendar;
import java.util.Date;

@Component
public class JwtUtil {
    private static final SecretKey secretKey = Jwts.SIG.HS256.key().build();
    private static final String CLAIM_KEY = "userName";
    private static final int ACCESS_TOKEN_VALIDATE_TIME = 1; //Minute
    private static final int REFRESH_TOKEN_VALIDATE_TIME = 3; //Day

    public String generateAccessToken(String userName){
        ObjectMapper mapper = new ObjectMapper();
        String accessToken = null;
        
        try {
            accessToken = Jwts.builder()
            .signWith(secretKey)
            .subject("AccessToken")
            .expiration(getAccessTokenValidateTime())
            .claim(CLAIM_KEY, mapper.writeValueAsString(userName))
            .compact();    
        } catch (JsonProcessingException e) {
            System.out.println("Generate Access Token Fail");
            e.printStackTrace();
        }
        
        return accessToken;
    }

    public String generateRefreshToken(String userName) {
        ObjectMapper mapper = new ObjectMapper();
        String refreshToken = null;
        
        try {
            refreshToken = Jwts.builder()
            .signWith(secretKey)
            .subject("RefreshToken")
            .expiration(getRefreshTokenValidateTime())
            .claim(CLAIM_KEY, mapper.writeValueAsString(userName))
            .compact();  
        } catch (JsonProcessingException e) {
            System.out.println("Generate Refresh Token Fail");
            e.printStackTrace();
        }
        return refreshToken;

    }

    public String parseToken(String token) {
        String userName = null;
        
        try {
            userName = (String) Jwts
                    .parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get(CLAIM_KEY);
            System.out.println("parseToken before userName : " + userName);
            userName = userName.replaceAll("\"", "");
            System.out.println("parseToken after userName : " + userName);
        } catch (SignatureException e) {
            System.out.println("Parse Token Fail");
        }
        return userName;
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

    public boolean isTokenValide(String token) {
        boolean result = false;
        
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            
            result = claims.getExpiration().before(new Date());
        } catch(SignatureException e) {
            System.out.println("Check TokenValide Fail");
        } catch(ExpiredJwtException e) {
            System.out.println("Token expired");
        }

        return result;
    }
}
