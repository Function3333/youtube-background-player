package com.rest.ybp.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rest.ybp.common.Result;
import com.rest.ybp.utils.JwtUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtManager;

    public UserService(UserRepository userRepository, JwtUtil jwtManager) {
        this.userRepository = userRepository;
        this.jwtManager = jwtManager;
    }

    @Transactional
    public Result signup(String name, String password, String email) {
        if(validateName(name) == Result.DUPLICATE_NAME) return Result.DUPLICATE_NAME;
        if(validateEmail(email) == Result.DUPLICATE_EMAIL) return Result.DUPLICATE_EMAIL;

        User user = new User(name, password, email);
        int result = userRepository.save(user);

        if(result == 0) return Result.SIGNUP_FAIL;
        else return Result.SUCCESS;
    }

    @Transactional
    public Map<String, String> login(String name, String password) throws JsonProcessingException {
        User findByName = userRepository.getUserByName(name);
        Map<String, String> tokenMap = null;

        if(findByName != null) {
            String userPassword = findByName.getPassword();

            if(userPassword.equals(password)) {
                tokenMap = new HashMap<>();
                tokenMap.put("accessToken", jwtManager.generateAccessToken(findByName.getName()));
                tokenMap.put("refreshToken", jwtManager.getRefreshToken(findByName.getName()));
            }
        }
        return tokenMap;
    }

    public boolean isTokenMatchUser(String accessToken) {
        String userName = jwtManager.parseAccessToken(accessToken);
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
