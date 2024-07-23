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
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
        .allowedOrigins("*") // 모든 출처 허용
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*");
    }

    @Override
    @SuppressWarnings("null")
    public void addInterceptors(InterceptorRegistry registry) {
        String[] excludePattern = {
            "/user", "/user/login", "/user/accessToken", "/login/validateUsername", "/login/verifyEmail"};

        registry.addInterceptor(jwtInterceptor)
                .excludePathPatterns(excludePattern);
    }
}
