package com.rest.ybp.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.ybp.common.Response;
import com.rest.ybp.common.Result;
import com.rest.ybp.utils.EmailUtil;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
public class UserController {
    private final UserService userService;
    private static final String VERIFY_EMAIL_SESSION_KEY= "certificationNumber";
    private static final String EMAIL_SESSION_KEY= "EMAIL";
    private static final int SESSION_DURATION= 30;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login/verifyEmail")
    public Response sendVerifyEmail(@RequestParam("inputEmail") String inputEmail , HttpSession session) throws IOException {
        String certificationNumber = EmailUtil.createCertificationNumber();

        EmailUtil email = new EmailUtil();
        Result result = email.sendEmail(inputEmail, certificationNumber);

        if(result == Result.SUCCESS) {
            session.setAttribute(VERIFY_EMAIL_SESSION_KEY, certificationNumber);
            session.setAttribute(EMAIL_SESSION_KEY, inputEmail);
            session.setMaxInactiveInterval(SESSION_DURATION);

            return new Response(result.toString(), result.getMsg());
        }

        return new Response(result.toString(), result.getMsg());
    }

    @PostMapping("/login/verifyEmail")
    public Response validateVerifyEmail(@RequestParam("inputNumber") String inputNumber, HttpSession session) {
        String certificationNumber = (String)session.getAttribute(VERIFY_EMAIL_SESSION_KEY);

        Result result = userService.validateEmail((String) session.getAttribute(EMAIL_SESSION_KEY));
        if(result == Result.DUPLICATE_EMAIL) return new Response(Result.DUPLICATE_EMAIL.name(), Result.DUPLICATE_EMAIL.getMsg());

        if(inputNumber.equals(certificationNumber)) return new Response(Result.SUCCESS.name(), Result.SUCCESS.getMsg());
        return new Response(Result.VERIFY_EMAIL_FAIL.toString(),Result.VERIFY_EMAIL_FAIL.getMsg());
    }

    @GetMapping("/login/validateName")
    public Response validateName(@RequestParam("name") String name) {
        Result result = userService.validateName(name);
        return new Response(result.name(), result.getMsg());
    }

    @PostMapping("/user")
    public Response signup(@ModelAttribute UserDto userDto) {
        Result result = userService.signup(userDto.getName(), userDto.getPassword(), userDto.getEmail());

        return new Response(result.getStatus(), result.getMsg());
    }

    @PostMapping("/user/login")
    public Response login(@RequestParam("name") String name, @RequestParam("password") String password) throws JsonProcessingException {
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
