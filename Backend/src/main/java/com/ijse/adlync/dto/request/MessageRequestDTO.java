package com.ijse.adlync.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestDTO {
    private Long senderUserId;
    private Long chatId;
    private String content;
    private LocalDateTime sent_at;
}
