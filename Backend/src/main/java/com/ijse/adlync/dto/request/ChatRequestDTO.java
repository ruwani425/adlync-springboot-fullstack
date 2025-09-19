package com.ijse.adlync.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatRequestDTO {
    private Long clientUserId;
    private Long ownerUserId;
    private Long postId;
    private String firstMessage;
}
