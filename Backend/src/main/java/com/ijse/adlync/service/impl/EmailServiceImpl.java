package com.ijse.adlync.service.impl;

import com.ijse.adlync.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public void sendSignupEmail(String to, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Welcome to Adlync Marketplace!");

        String text = "Hi " + name + "!\n\n" +
                "Thank you for signing up for an account on Adlync Marketplace.\n\n" +
                "If you didn't sign up for this account, or need help, contact us at support@adlync.com\n\n" +
                "Regards,\n" +
                "The Adlync Marketplace Team\n" +
                "--------------------------------------------\n" +
                "Adlync - Your Marketplace in Sri Lanka\n" +
                "Follow us on Facebook: https://www.facebook.com/adlync\n" +
                "Download our app: https://adlync.com/app";

        message.setText(text);
        mailSender.send(message);
    }
}
