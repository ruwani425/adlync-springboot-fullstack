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
    private Long toUser_id;
    private LocalDateTime sent_at;
    private String content;
    private Long postId;

    public MessageResponseDTO(Long fromUserId, String content, String room) {
        this.message_id = fromUserId;
        this.content = content;
        this.postId = fromUserId;
        this.sent_at = LocalDateTime.now();
    }
}
