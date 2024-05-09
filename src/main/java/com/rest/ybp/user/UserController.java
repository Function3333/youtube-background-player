package com.rest.ybp.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.ybp.common.Response;
import com.rest.ybp.common.Result;
import com.rest.ybp.utils.EmailUtil;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    private static final int SESSION_DURATION= 60 * 5;
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login/verifyEmail")
    public Response sendVerifyEmail(@RequestParam("email") String inputEmail , HttpSession session) throws IOException {
        String certificationNumber = EmailUtil.createCertificationNumber();

        EmailUtil email = new EmailUtil();
        Result result = email.sendEmail(inputEmail, certificationNumber);

        if(result == Result.SUCCESS) {
            session.setAttribute(inputEmail, certificationNumber);
            session.setMaxInactiveInterval(SESSION_DURATION);

            return new Response(result.toString(), result.getMsg());
        }

        return new Response(result.toString(), result.getMsg());
    }

    @PostMapping("/login/verifyEmail")
    public Response validateVerifyEmail(@RequestBody HashMap<String,String> jsonMap, HttpSession session) {
        String verifyNumber = jsonMap.get("verifyNumber");
        String inputEmail = jsonMap.get("inputEmail");
        String certificationNumber = (String) session.getAttribute(inputEmail);

        if(inputEmail != null) {
            Result result = userService.validateEmail(inputEmail);
            if(verifyNumber.equals(certificationNumber) && result == Result.DUPLICATE_EMAIL) return new Response(Result.DUPLICATE_EMAIL.name(), Result.DUPLICATE_EMAIL.getMsg());
            if(verifyNumber.equals(certificationNumber)) return new Response(Result.SUCCESS.name(), Result.SUCCESS.getMsg());
        }        
        return new Response(Result.VERIFY_EMAIL_FAIL.toString(),Result.VERIFY_EMAIL_FAIL.getMsg());
    }

    @GetMapping("/login/validateUsername")
    public Response validateName(@RequestParam("username") String name) {
        Result result = userService.validateName(name);
        return new Response(result.name(), result.getMsg());
    }

    @PostMapping("/user")
    public Response signup(@RequestBody HashMap<String, String> jsonMap) {
        String username = jsonMap.get("username");
        String email = jsonMap.get("email");
        String password = jsonMap.get("password");

        Result result = userService.signup(username, password, email);

        return new Response(result.getStatus(), result.getMsg());
    }

    @PostMapping("/user/login")
    public Response login(@RequestBody HashMap<String, String> jsonMap) throws JsonProcessingException {
        String name =  jsonMap.get("name");
        String password =  jsonMap.get("password");
        
        Map<String, String> tokenMap = userService.login(name, password);

        if(tokenMap != null) {
            ObjectMapper mapper = new ObjectMapper();
            return new Response(Result.SUCCESS.toString(), mapper.writeValueAsString(tokenMap));
        }

        return new Response(Result.LOGIN_FAIL.toString(), Result.LOGIN_FAIL.getMsg());
    }

    @GetMapping("/user/accessToken")
    public Response getAccessTokenByRefreshToken(@RequestHeader("REFRESH_TOKEN") String refreshToken) {
        String accessToken = userService.getAccessTokenByRefreshToken(refreshToken);
        Result reuslt = (accessToken == null) ? Result.PARSE_TOKEN_FAIL : Result.SUCCESS;

        return new Response(reuslt.getStatus(), accessToken);
    }
}