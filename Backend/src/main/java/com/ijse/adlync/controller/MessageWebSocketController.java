package com.ijse.adlync.controller;

import com.ijse.adlync.dto.request.MessageRequestDTO;
import com.ijse.adlync.dto.response.MessageResponseDTO;
import com.ijse.adlync.service.impl.MessageServiceImpl;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MessageWebSocketController {
    private final SimpMessagingTemplate template;
    private final MessageServiceImpl service;

    public MessageWebSocketController(SimpMessagingTemplate template, MessageServiceImpl service) {
        this.template = template;
        this.service = service;
    }

    @MessageMapping("/send-message")
    public void sendMessage(MessageRequestDTO dto) {
        MessageResponseDTO savedMessage = service.sendMessage(dto);
        template.convertAndSend("/topic/messages/" + dto.getChatId(), savedMessage);
    }
}
