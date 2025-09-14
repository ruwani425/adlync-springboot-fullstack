package com.ijse.adlync.service;

import org.springframework.stereotype.Service;

public interface EmailService {
     void sendSignupEmail(String to, String name);
    void sendModeratorSignupEmail(String to, String name) throws Exception;

}
