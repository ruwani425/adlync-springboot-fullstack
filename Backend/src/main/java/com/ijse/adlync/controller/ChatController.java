package com.ijse.adlync.controller;

import com.ijse.adlync.dto.ChatMessage;
import com.ijse.adlync.dto.request.ChatRequestDTO;
import com.ijse.adlync.dto.request.MessageRequestDTO;
import com.ijse.adlync.dto.response.ChatResponseDTO;
import com.ijse.adlync.dto.response.MessageResponseDTO;
import com.ijse.adlync.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Controller
@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/message/{chatId}")
    @SendTo("/topic/chat/{chatId}")
    public ChatMessage sendMessage(@DestinationVariable String chatId, ChatMessage message) {
        try {
            MessageRequestDTO requestDTO = new MessageRequestDTO();
            requestDTO.setChatId(Long.parseLong(chatId));
            requestDTO.setSenderUserId(Long.parseLong(message.getFrom()));
            requestDTO.setContent(message.getContent());
            requestDTO.setSent_at(LocalDateTime.now());

            chatService.sendMessage(requestDTO);

        } catch (Exception e) {
            System.err.println("Error saving message: " + e.getMessage());
        }

        message.setTimestamp(new Date());
        return message;
    }

    @PostMapping("/create")
    public ResponseEntity<ChatResponseDTO> createOrGetChat(@RequestBody ChatRequestDTO requestDTO) {
        try {
            System.out.println("Creating chat with request: " + requestDTO);
            ChatResponseDTO response = chatService.createOrGetChat(requestDTO);
            System.out.println("Chat created successfully: " + response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error creating chat: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<MessageResponseDTO>> getChatMessages(@PathVariable Long chatId) {
        List<MessageResponseDTO> messages = chatService.getChatMessages(chatId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatResponseDTO>> getUserChats(@PathVariable Long userId) {
        List<ChatResponseDTO> chats = chatService.getChatsByUserId(userId);
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/post/{postId}/chats")
    public ResponseEntity<List<ChatResponseDTO>> getChatsByPostId(@PathVariable Long postId) {
        List<ChatResponseDTO> chats = chatService.getChatsByPostId(postId);
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/between/{postId}/{userId1}/{userId2}")
    public ResponseEntity<ChatResponseDTO> getChatBetweenUsers(
            @PathVariable Long postId,
            @PathVariable Long userId1,
            @PathVariable Long userId2) {
        ChatResponseDTO chat = chatService.findChatBetweenUsers(postId, userId1, userId2);
        if (chat != null) {
            return ResponseEntity.ok(chat);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/message")
    public ResponseEntity<MessageResponseDTO> sendDirectMessage(@RequestBody MessageRequestDTO requestDTO) {
        MessageResponseDTO response = chatService.sendMessage(requestDTO);

        ChatMessage chatMessage = ChatMessage.builder()
                .from(requestDTO.getSenderUserId().toString())
                .content(requestDTO.getContent())
                .timestamp(new Date())
                .build();

        messagingTemplate.convertAndSend("/topic/chat/" + requestDTO.getChatId(), chatMessage);

        return ResponseEntity.ok(response);
    }
}
