package com.rest.ybp;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.rest.ybp.interceptor.JwtInterceptor;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
    private JwtInterceptor jwtInterceptor;

    public WebMvcConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }
    
    @Override
    @SuppressWarnings("null")
    public void addInterceptors(InterceptorRegistry registry) {
        String host = "https://api.function3333.com";
        String[] excludePattern = 
            {
                host + "/user", 
                host + "/user/login", 
                host + "/user/accessToken", 
                host + "/login/validateUsername", 
                host + "/login/verifyEmail"
            };

        registry.addInterceptor(jwtInterceptor)
                .excludePathPatterns(excludePattern);
    }
}
