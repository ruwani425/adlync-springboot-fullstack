package com.ijse.adlync.service;

public interface EmailService {
    void sendSignupEmail(String to, String name);

    void sendModeratorSignupEmail(String to, String name) throws Exception;

    public void sendOTPEmail(String email, String otp);
}
