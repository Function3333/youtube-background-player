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
    private static final int ACCESS_TOKEN_VALIDATE_TIME = 30; //Minute
    private static final int REFRESH_TOKEN_VALIDATE_TIME = 3; //Day

    public String generateAccessToken(String userName){
        ObjectMapper mapper = new ObjectMapper();
        String accessToken = null;
        
        try {
            accessToken = Jwts.builder()
            .signWith(secretKey)
            .subject("AccessToken")
            .expiration(getAccessTokenExpireDate())
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
            .expiration(getRefreshTokenExpireDate())
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
            userName = userName.replaceAll("\"", "");
        } catch (SignatureException e) {
            System.out.println("Parse Token Fail");
        }
        return userName;
    }

    public Date getAccessTokenExpireDate() {
        Date now = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        calendar.add(Calendar.MINUTE, ACCESS_TOKEN_VALIDATE_TIME);
        return calendar.getTime();
    }

    public Date getRefreshTokenExpireDate() {
        Date now = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        calendar.add(Calendar.DAY_OF_WEEK, REFRESH_TOKEN_VALIDATE_TIME);
        return calendar.getTime();
    }

    public Date getTokenExpiration(String token) {
        Date expiredDate = null;

        try {
            Claims claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();    
            expiredDate = claims.getExpiration();
        } catch (SignatureException | ExpiredJwtException e) {
            e.printStackTrace();
        }
        return expiredDate;
    }

    public boolean isTokenExpired(String token) {
        boolean result = true;

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            result = claims.getExpiration().before(new Date());
        } catch(SignatureException e) {
            System.out.println("[JwtUtil] Token is not valide");
        } catch(ExpiredJwtException e) {
            e.printStackTrace();
            System.out.println("[JwtUtil] Token Expired");
        }

        return result;
    }
}