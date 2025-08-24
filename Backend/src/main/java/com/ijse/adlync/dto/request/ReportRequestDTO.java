package com.ijse.adlync.dto.request;

import java.time.LocalDateTime;

public class ReportRequestDTO {

    private String reason;
    private LocalDateTime date;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
