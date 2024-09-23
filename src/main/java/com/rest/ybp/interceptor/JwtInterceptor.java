package com.rest.ybp.interceptor;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.ybp.common.Response;
import com.rest.ybp.common.Result;
import com.rest.ybp.user.UserService;
import com.rest.ybp.utils.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor{
    private static final String ACCESS_TOKEN_HEADER_KEY = "ACCESS_TOKEN";
    private static final String REFRESH_TOKEN_HEADER_KEY = "REFRESH_TOKEN";

    private final Properties awsConfig = new Properties();
    private final UserService userService;    
    private final JwtUtil jwtUtil;

    public JwtInterceptor(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        
        initProperties();
    }

    public void initProperties() {
        try {
            awsConfig.load(this.getClass().getResourceAsStream("/config.properties"));    
        } catch (IOException e) {
            System.out.println("[JwtInterceptor] Init Properties File Failed!");
        }
    }
    
    @Override
    @SuppressWarnings("null")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {    
        ObjectMapper mapper = new ObjectMapper();
        Result result = null;
        boolean interceptorResult = false;
        
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            
            System.out.println("HeaderName : " + headerName);
            System.out.println("HeaderValue : " + request.getHeader(headerName));
        }

        String accessToken = request.getHeader(ACCESS_TOKEN_HEADER_KEY);
        String refreshToken = request.getHeader(REFRESH_TOKEN_HEADER_KEY);

        if(isTokenNullOrEmpty(accessToken) || isTokenNullOrEmpty(refreshToken)) {
            result = Result.EMPTY_TOKEN_FAIL;
        } else {
            if(jwtUtil.isTokenExpired(accessToken) == true) {
                result = Result.EXPIRE_ACCESS_TOKEN_FAIL;
    
                if(jwtUtil.isTokenExpired(refreshToken) == true) result = Result.EXPIRE_REFRESH_TOKEN_FAIL;
            }     
        }
        
        if(result == null) {
            interceptorResult = userService.isTokenMatchUser(accessToken);
        }
        
        if(result != null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            Response logicResponse = new Response(result.getStatus(), result.getMsg());
            response.getWriter().write(mapper.writeValueAsString(logicResponse));
        } 

        return interceptorResult;
    }

    public boolean isTokenNullOrEmpty(String token) {
        if(token == null || token.isEmpty()) return true;
        return false;
    }
}
