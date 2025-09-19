package com.ijse.adlync.service.impl;

import com.ijse.adlync.dto.request.ChatRequestDTO;
import com.ijse.adlync.dto.request.MessageRequestDTO;
import com.ijse.adlync.dto.response.ChatResponseDTO;
import com.ijse.adlync.dto.response.MessageResponseDTO;
import com.ijse.adlync.entity.ChatEntity;
import com.ijse.adlync.entity.MessageEntity;
import com.ijse.adlync.entity.PostEntity;
import com.ijse.adlync.entity.UserEntity;
import com.ijse.adlync.repository.ChatRepository;
import com.ijse.adlync.repository.MessageRepository;
import com.ijse.adlync.repository.PostRepository;
import com.ijse.adlync.repository.UserRepository;
import com.ijse.adlync.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    @Transactional
    public ChatResponseDTO createOrGetChat(ChatRequestDTO requestDTO) {
        try {
            System.out.println("ChatService: Creating/getting chat for request: " + requestDTO);
            
            // First check if chat already exists
            Optional<ChatEntity> existingChat = chatRepository.findChatBetweenUsers(
                requestDTO.getPostId(), 
                requestDTO.getClientUserId(), 
                requestDTO.getOwnerUserId()
            );

            ChatEntity chat;
            if (existingChat.isPresent()) {
                System.out.println("ChatService: Found existing chat");
                chat = existingChat.get();
            } else {
                System.out.println("ChatService: Creating new chat");
                // Create new chat
                chat = new ChatEntity();
                
                System.out.println("ChatService: Finding client user with ID: " + requestDTO.getClientUserId());
                UserEntity clientUser = userRepository.findById(requestDTO.getClientUserId())
                    .orElseThrow(() -> new RuntimeException("Client user not found with ID: " + requestDTO.getClientUserId()));
                    
                System.out.println("ChatService: Finding owner user with ID: " + requestDTO.getOwnerUserId());
                UserEntity ownerUser = userRepository.findById(requestDTO.getOwnerUserId())
                    .orElseThrow(() -> new RuntimeException("Owner user not found with ID: " + requestDTO.getOwnerUserId()));
                    
                System.out.println("ChatService: Finding post with ID: " + requestDTO.getPostId());
                PostEntity post = postRepository.findById(requestDTO.getPostId())
                    .orElseThrow(() -> new RuntimeException("Post not found with ID: " + requestDTO.getPostId()));

                chat.setClientUser(clientUser);
                chat.setOwnerUser(ownerUser);
                chat.setPost(post);
                chat.setCreated_at(LocalDateTime.now());
                chat.setLast_message_at(LocalDateTime.now());

                System.out.println("ChatService: Saving chat to database");
                chat = chatRepository.save(chat);
                System.out.println("ChatService: Chat saved with ID: " + chat.getChat_id());
            }

            // Send first message if provided
            if (requestDTO.getFirstMessage() != null && !requestDTO.getFirstMessage().trim().isEmpty()) {
                System.out.println("ChatService: Sending first message");
                MessageRequestDTO messageRequest = new MessageRequestDTO();
                messageRequest.setChatId(chat.getChat_id());
                messageRequest.setSenderUserId(requestDTO.getClientUserId());
                messageRequest.setContent(requestDTO.getFirstMessage());
                messageRequest.setSent_at(LocalDateTime.now());
                
                sendMessage(messageRequest);
                System.out.println("ChatService: First message sent");
            }

            ChatResponseDTO response = toChatResponseDTO(chat);
            System.out.println("ChatService: Returning response: " + response);
            return response;
            
        } catch (Exception e) {
            System.err.println("ChatService: Error in createOrGetChat: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public ChatResponseDTO findChatById(Long chatId) {
        ChatEntity chat = chatRepository.findById(chatId)
            .orElseThrow(() -> new RuntimeException("Chat not found"));
        return toChatResponseDTO(chat);
    }

    @Override
    public List<ChatResponseDTO> getChatsByUserId(Long userId) {
        return chatRepository.findChatsByUserId(userId).stream()
            .map(this::toChatResponseDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<ChatResponseDTO> getChatsByPostId(Long postId) {
        return chatRepository.findChatsByPostId(postId).stream()
            .map(this::toChatResponseDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<MessageResponseDTO> getChatMessages(Long chatId) {
        return messageRepository.findMessagesByChatId(chatId).stream()
            .map(this::toMessageResponseDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MessageResponseDTO sendMessage(MessageRequestDTO requestDTO) {
        ChatEntity chat = chatRepository.findById(requestDTO.getChatId())
            .orElseThrow(() -> new RuntimeException("Chat not found"));
        
        UserEntity sender = userRepository.findById(requestDTO.getSenderUserId())
            .orElseThrow(() -> new RuntimeException("Sender user not found"));

        MessageEntity message = new MessageEntity();
        message.setChat(chat);
        message.setSenderUser(sender);
        message.setContent(requestDTO.getContent());
        message.setSent_at(LocalDateTime.now());

        message = messageRepository.save(message);

        // Update chat's last message time
        chat.setLast_message_at(LocalDateTime.now());
        chatRepository.save(chat);

        return toMessageResponseDTO(message);
    }

    @Override
    public ChatResponseDTO findChatBetweenUsers(Long postId, Long userId1, Long userId2) {
        Optional<ChatEntity> chat = chatRepository.findChatBetweenUsers(postId, userId1, userId2);
        return chat.map(this::toChatResponseDTO).orElse(null);
    }

    private ChatResponseDTO toChatResponseDTO(ChatEntity entity) {
        ChatResponseDTO dto = new ChatResponseDTO();
        dto.setChat_id(entity.getChat_id());
        dto.setClientUserId(entity.getClientUser().getId());
        dto.setClientUserName(entity.getClientUser().getName());
        dto.setOwnerUserId(entity.getOwnerUser().getId());
        dto.setOwnerUserName(entity.getOwnerUser().getName());
        dto.setPostId(entity.getPost().getPost_id());
        dto.setPostTitle(entity.getPost().getTitle());
        dto.setCreated_at(entity.getCreated_at());
        dto.setLast_message_at(entity.getLast_message_at());
        
        // Get last message
        List<MessageEntity> messages = messageRepository.findMessagesByChatId(entity.getChat_id());
        if (!messages.isEmpty()) {
            MessageEntity lastMessage = messages.get(messages.size() - 1);
            dto.setLastMessage(lastMessage.getContent());
        }
        
        return dto;
    }

    private MessageResponseDTO toMessageResponseDTO(MessageEntity entity) {
        MessageResponseDTO dto = new MessageResponseDTO();
        dto.setMessage_id(entity.getMessage_id());
        dto.setSenderUserId(entity.getSenderUser().getId());
        dto.setSenderUserName(entity.getSenderUser().getName());
        dto.setChatId(entity.getChat().getChat_id());
        dto.setSent_at(entity.getSent_at());
        dto.setContent(entity.getContent());
        return dto;
    }
}
