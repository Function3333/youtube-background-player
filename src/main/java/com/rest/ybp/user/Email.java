package com.rest.ybp.user;

import com.rest.ybp.common.Result;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

public class Email {
    private static Properties emailProperties;

    public Result sendEmail(String receiverEmail) throws IOException {
        initProperties();
        Properties smtpProperties = createSmtpProperties();
        Session session = createSession(smtpProperties);

        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(emailProperties.getProperty("email.id")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmail));
            message.setSubject("test Email");
            message.setText("안녕하세요 테스트입니다.");

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.SEND_MAIL_FAIL;
        }

        return Result.SUCCESS;
    }

    public void initProperties() throws IOException {
        emailProperties = new Properties();
        emailProperties.load(this.getClass().getResourceAsStream("/email.properties"));
    }

    public Properties createSmtpProperties() {
        Properties smtpProperties = new Properties();
        smtpProperties.put("mail.smtp.host","smtp.naver.com");
        smtpProperties.put("mail.smtp.port","465");
        smtpProperties.put("mail.smtp.ssl.enable", "true");
        smtpProperties.put("mail.smtp.ssl.trust", "smtp.naver.com");
        smtpProperties.put("mail.smtp.auth", "true");

        return smtpProperties;
    }

    public Session createSession(Properties smtpProperties) {
        Session session = Session.getDefaultInstance(smtpProperties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailProperties.getProperty("email.id"), emailProperties.getProperty("email.password"));
            }
        });

        return session;
    }
}
