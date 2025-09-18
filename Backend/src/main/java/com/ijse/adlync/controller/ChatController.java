package com.ijse.adlync.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    @MessageMapping("/message/{room}")
    @SendTo("/topic/chat/{room}")
    public ChatMessage sendMessage(@DestinationVariable String room, ChatMessage message) {
        return message;
    }
}
