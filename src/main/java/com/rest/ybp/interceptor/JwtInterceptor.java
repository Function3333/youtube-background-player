package com.rest.ybp.interceptor;

import java.io.IOException;

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
    private final UserService userService;    
    
    public JwtInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {    
        System.out.println("run interceptor");

        boolean result = false;
        ObjectMapper mapper = new ObjectMapper();
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            String accessToken = request.getHeader("AUTHORIZATION");

            if(accessToken != null) {
                result = userService.isTokenMatchUser(accessToken);
            }else {
                Response failResponse = new Response(Result.EMPTY_TOKEN_FAIL.getStatus(), Result.EMPTY_TOKEN_FAIL.getMsg());
                response.getWriter().write(mapper.writeValueAsString(failResponse));
            }           
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
