package com.rest.ybp.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rest.ybp.common.Result;
import com.rest.ybp.common.Token;
import com.rest.ybp.utils.HashUtil;
import com.rest.ybp.utils.JwtUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public Result signup(String name, String password, String email) {
        Result result = Result.SIGNUP_FAIL;
        
        try {
            if(validateName(name) == Result.SUCCESS && validateEmail(email) == Result.SUCCESS) {
                String hashedPassword = HashUtil.hashPlanPassword(password);
                
                if(hashedPassword != null) {
                    User user = new User(name, hashedPassword, email);
                    userRepository.save(user);    
                    result = Result.SUCCESS;
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("[UserService] singUp Failed");
            e.printStackTrace();
        }
        return result;
    }

    public Map<String, Token> login(String name, String password) throws JsonProcessingException {
        Map<String, Token> tokenMap = null;

        User user = userRepository.getUserByName(name);
        if(user != null) {
            String dbPassword = user.getPassword();

            if(HashUtil.isPwdMatchHashPwd(password, dbPassword)) {
                tokenMap = new HashMap<>();
                
                String accessTokenPayload = jwtUtil.generateAccessToken(user.getName());
                String refreshTokenPayload = jwtUtil.generateRefreshToken(user.getName());

                tokenMap.put("accessToken", new Token(accessTokenPayload, jwtUtil.getTokenExpiration(accessTokenPayload)));
                tokenMap.put("refreshToken", new Token(refreshTokenPayload, jwtUtil.getTokenExpiration(refreshTokenPayload)));
            }
        }
        return tokenMap;
    }

    public Token getAccessTokenByRefreshToken(String refreshToken) {
        Token token = null;

        if(isTokenMatchUser(refreshToken) && !jwtUtil.isTokenExpired(refreshToken)) {
            String userName = jwtUtil.parseToken(refreshToken);
            
            String accessTokenPayload = jwtUtil.generateAccessToken(userName);
            Date accessTokenExpiredDate = jwtUtil.getTokenExpiration(accessTokenPayload);

            token = new Token(accessTokenPayload, accessTokenExpiredDate);
        }
        return token;
    }

    public boolean isTokenMatchUser(String accessToken) {
        String userName = jwtUtil.parseToken(accessToken);
        User user = getUserByName(userName);    
        
        return (user != null) ? true : false;
    }

    public User getUserByName(String name) {
        return userRepository.getUserByName(name);
    }

    public User getUserByEmail(String email){
        return userRepository.getUserByEmail(email);
    }

    public Result validateName(String name) {
        return isDuplicateName(name) ? Result.DUPLICATE_NAME : Result.SUCCESS;
    }

    public Result validateEmail(String email) {
        return isDuplicateEmail(email) ? Result.DUPLICATE_EMAIL : Result.SUCCESS;
    }

    public boolean isDuplicateName(String name) {
        User user = getUserByName(name);
        return user != null;
    }

    public boolean isDuplicateEmail(String email) {
        User user = getUserByEmail(email);
        return user != null;
    }
}
