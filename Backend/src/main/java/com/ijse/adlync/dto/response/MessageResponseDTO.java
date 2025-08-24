package com.ijse.adlync.dto.response;

import java.time.LocalDateTime;

public class MessageResponseDTO {

    private Long message_id;
    private LocalDateTime sent_at;
    private String content;

    public Long getMessage_id() {
        return message_id;
    }

    public void setMessage_id(Long message_id) {
        this.message_id = message_id;
    }

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
