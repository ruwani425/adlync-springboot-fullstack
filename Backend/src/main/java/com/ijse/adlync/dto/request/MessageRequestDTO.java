package com.ijse.adlync.dto.request;

import java.time.LocalDateTime;

public class MessageRequestDTO {

    private LocalDateTime sent_at;
    private String content;

    public LocalDateTime getSent_at() {
        return sent_at;
    }

    public void setSent_at(LocalDateTime sent_at) {
        this.sent_at = sent_at;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
