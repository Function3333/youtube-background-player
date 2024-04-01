package com.rest.ybp.utils;

import com.rest.ybp.common.Result;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class EmailUtil {
    private static Properties emailProperties;
    private static Properties smtpProperties;
    private static final int CERTIFICATION_NUMBER_LENGTH = 6;

    public EmailUtil() throws IOException {
        initProperties();
        initSmtpProperties();
    }

    public Result sendEmail(String receiverEmail, String certificationNumber) throws IOException {
        Session session = createSession(smtpProperties);

        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(emailProperties.getProperty("email.id")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmail));
            message.setSubject("test Email");
            message.setText("인증번호 : " + certificationNumber);

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

    public void initSmtpProperties() {
        smtpProperties = new Properties();
        smtpProperties.put("mail.smtp.host","smtp.naver.com");
        smtpProperties.put("mail.smtp.port","465");
        smtpProperties.put("mail.smtp.ssl.enable", "true");
        smtpProperties.put("mail.smtp.ssl.trust", "smtp.naver.com");
        smtpProperties.put("mail.smtp.auth", "true");
    }

    public Session createSession(Properties smtpProperties) {

        return Session.getDefaultInstance(smtpProperties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailProperties.getProperty("email.id"), emailProperties.getProperty("email.password"));
            }
        });
    }

    public static String createCertificationNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0;  i < CERTIFICATION_NUMBER_LENGTH; i++) {
            sb.append(random.nextInt(9));
        }
        return sb.toString();
    }
}
