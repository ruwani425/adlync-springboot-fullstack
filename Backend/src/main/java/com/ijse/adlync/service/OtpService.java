package com.ijse.adlync.service;

public interface OtpService {
    void generateAndSendOTP(String email) throws Exception;
    boolean verifyOTP(String email, String otp);
    void clearOTP(String email);
}
