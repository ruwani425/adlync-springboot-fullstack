package com.ijse.adlync.service.impl;

import com.ijse.adlync.service.EmailService;
import com.ijse.adlync.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService {
    private final Map<String, OtpInfo> otpStorage = new HashMap<>();
    private final EmailService emailService;

    @Autowired
    public OtpServiceImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    private String generateOTP() {
        Random random = new Random();
        int otpValue = 100000 + random.nextInt(900000);
        return String.valueOf(otpValue);
    }

    @Override
    public void generateAndSendOTP(String email) throws Exception {
        String otp = generateOTP();
        LocalDateTime expiry = LocalDateTime.now().plus(5, ChronoUnit.MINUTES); // 5-minute expiry
        otpStorage.put(email, new OtpInfo(otp, expiry));
        emailService.sendOTPEmail(email, otp);
    }

    @Override
    public boolean verifyOTP(String email, String otp) {
        OtpInfo info = otpStorage.get(email);
        if (info == null || LocalDateTime.now().isAfter(info.getExpiry())) {
            otpStorage.remove(email);
            return false;
        }
        if (!info.getOtp().equals(otp)) {
            return false;
        }
        otpStorage.remove(email);
        return true;
    }

    @Override
    public void clearOTP(String email) {
        otpStorage.remove(email);
    }

    private static class OtpInfo {
        private final String otp;
        private final LocalDateTime expiry;

        public OtpInfo(String otp, LocalDateTime expiry) {
            this.otp = otp;
            this.expiry = expiry;
        }

        public String getOtp() {
            return otp;
        }

        public LocalDateTime getExpiry() {
            return expiry;
        }
    }
}
