package com.ijse.adlync.service.impl;

import com.ijse.adlync.service.EmailService;
import com.ijse.adlync.util.ValueEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final ValueEncoder valueEncoder;

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

    @Override
    public void sendModeratorSignupEmail(String email, String name) throws Exception {

        String signupLink = "http://127.0.0.1:5500/pages/modaratorsignup.html?token=" + valueEncoder.encrypt(email);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Moderator Account Created - Adlync Marketplace");

        String text = "Hi " + name + ",\n\n" +
                "You have been invited as a moderator on Adlync Marketplace.\n" +
                "Please click the link below to complete your account setup:\n\n" +
                signupLink + "\n\n" +
                "If you did not expect this invitation, ignore this email.\n\n" +
                "Regards,\n" +
                "Adlync Team";

        message.setText(text);
        mailSender.send(message);
    }

    @Override
    public void sendOTPEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset OTP");
        message.setText("Your OTP for password reset is: " + otp + ". It expires in 5 minutes.\n\nBest regards,\nAdlync Team");
        mailSender.send(message);
    }
}
