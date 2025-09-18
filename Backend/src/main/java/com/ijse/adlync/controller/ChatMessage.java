package com.ijse.adlync.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private String from;
    private String content;
    private Date timestamp;
}
