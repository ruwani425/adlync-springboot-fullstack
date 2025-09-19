package com.ijse.adlync.repository;

import com.ijse.adlync.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
    
    @Query("SELECT c FROM ChatEntity c WHERE c.post.post_id = :postId AND ((c.clientUser.id = :userId1 AND c.ownerUser.id = :userId2) OR (c.clientUser.id = :userId2 AND c.ownerUser.id = :userId1))")
    Optional<ChatEntity> findChatBetweenUsers(@Param("postId") Long postId, @Param("userId1") Long userId1, @Param("userId2") Long userId2);
    
    @Query("SELECT c FROM ChatEntity c WHERE c.clientUser.id = :userId OR c.ownerUser.id = :userId ORDER BY c.last_message_at DESC")
    List<ChatEntity> findChatsByUserId(@Param("userId") Long userId);
    
    @Query("SELECT c FROM ChatEntity c WHERE c.post.post_id = :postId ORDER BY c.last_message_at DESC")
    List<ChatEntity> findChatsByPostId(@Param("postId") Long postId);
}
