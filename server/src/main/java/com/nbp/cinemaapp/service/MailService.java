package com.nbp.cinemaapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${FRONTEND_URL}")
    private String FRONTEND_URL;

    public MailService(final JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(final String email, final String token) {
        final String resetUrl = FRONTEND_URL + "/reset-password?token=" + token;
        final String body = "Click the link below to reset your password.\n" + resetUrl;

        final SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset");
        message.setText(body);

        mailSender.send(message);
    }
}
