package com.ijse.adlync.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MessageResponseDTO {

    private Long message_id;
    private LocalDateTime sent_at;
    private String content;
}
