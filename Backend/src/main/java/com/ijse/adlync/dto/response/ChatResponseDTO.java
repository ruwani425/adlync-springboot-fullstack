package com.ijse.adlync.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ChatResponseDTO {
    private Long chat_id;
    private Long clientUserId;
    private String clientUserName;
    private Long ownerUserId;
    private String ownerUserName;
    private Long postId;
    private String postTitle;
    private LocalDateTime created_at;
    private LocalDateTime last_message_at;
    private String lastMessage;
    private Long unreadCount;
}
