package com.rest.ybp.user;

import com.rest.ybp.common.Response;
import com.rest.ybp.common.Result;
import com.rest.ybp.utils.Email;
import com.rest.ybp.utils.JwtManager;
import jakarta.servlet.http.HttpSession;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class UserController {
    private final UserService userService;
    private static final String VERIFY_EMAIL_SESSION_KEY= "certificationNumber";
    private static final int SESSION_DURATION= 30;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login/verifyEmail")
    public Response sendVerifyEmail(@RequestParam("inputEmail") String inputEmail , HttpSession session) throws IOException {
        String certificationNumber = Email.createCertificationNumber();

        Email email = new Email();
        Result result = email.sendEmail(inputEmail, certificationNumber);

        if(result == Result.SUCCESS) {
            session.setAttribute(VERIFY_EMAIL_SESSION_KEY, certificationNumber);
            session.setMaxInactiveInterval(SESSION_DURATION);

            return new Response(result, result.getMsg());
        }

        return new Response(result, result.getMsg());
    }

    @PostMapping("/login/verifyEmail")
    public Response validateVerifyEmail(@RequestParam("inputNumber") String inputNumber, HttpSession session) {
        String certificationNumber = (String)session.getAttribute(VERIFY_EMAIL_SESSION_KEY);

        if(inputNumber.equals(certificationNumber)) return new Response(Result.SUCCESS, Result.SUCCESS.getMsg());
        return new Response(Result.VERIFY_EMAIL_FAIL,Result.VERIFY_EMAIL_FAIL.getMsg());
    }

    @GetMapping("/user/jwt")
    public Response getJwtToken(HttpResponse response) throws IOException {
        JwtManager jwtManager = new JwtManager();

        User user = new User("jwtName", "different", "jwtEmail");
        String jwt = jwtManager.createJwtToken(user);

        return new Response(Result.SUCCESS, jwt);
    }

    @GetMapping("/user/login")
    public Response login(@RequestParam("jwtToken") String jwtToken) {
        JwtManager jwtManager = new JwtManager();
        jwtManager.parseJwtToken(jwtToken);

        return new Response(Result.SUCCESS, "void");
    }
}
