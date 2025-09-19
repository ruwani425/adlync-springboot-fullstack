package com.ijse.adlync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ijse.adlync.entity.MessageEntity;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    @Query("SELECT m FROM MessageEntity m WHERE m.chat.chat_id = :chatId ORDER BY m.sent_at ASC")
    List<MessageEntity> findMessagesByChatId(@Param("chatId") Long chatId);
}
