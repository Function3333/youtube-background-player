package com.rest.ybp.user;

import com.rest.ybp.common.Response;
import com.rest.ybp.common.Result;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class UserController {
    private static final String VERIFY_EMAIL_SESSION_KEY= "certificationNumber";
    private static final int SESSION_DURATION= 30;

    @GetMapping("/login/verifyEmail")
    public Response sendVerifyEmail(HttpSession session) throws IOException {
        String receiverEmail = "whddlsk123@naver.com";
        String certificationNumber = Email.createCertificationNumber();

        Email email = new Email();
        Result result = email.sendEmail(receiverEmail, certificationNumber);

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
}
