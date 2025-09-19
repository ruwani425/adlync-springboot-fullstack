package com.ijse.adlync.service;

import com.ijse.adlync.dto.request.ChatRequestDTO;
import com.ijse.adlync.dto.request.MessageRequestDTO;
import com.ijse.adlync.dto.response.ChatResponseDTO;
import com.ijse.adlync.dto.response.MessageResponseDTO;

import java.util.List;

public interface ChatService {
    
    ChatResponseDTO createOrGetChat(ChatRequestDTO requestDTO);
    
    ChatResponseDTO findChatById(Long chatId);
    
    List<ChatResponseDTO> getChatsByUserId(Long userId);
    
    List<ChatResponseDTO> getChatsByPostId(Long postId);
    
    List<MessageResponseDTO> getChatMessages(Long chatId);
    
    MessageResponseDTO sendMessage(MessageRequestDTO requestDTO);
    
    ChatResponseDTO findChatBetweenUsers(Long postId, Long userId1, Long userId2);
}
