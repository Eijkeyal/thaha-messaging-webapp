package com.chat.app.repository;

import com.chat.app.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageRepository
        extends JpaRepository<Message, String> {


    List<Message> findByConversationIdOrderByCreatedAtAsc(
            String conversationId
    );
    List<Message> findByConversationId(
            String conversationId
    );


    Page<Message> findByConversationIdOrderByCreatedAtDesc(
            String conversationId,
            Pageable pageable
    );


    List<Message> findByConversationIdAndSenderIdNotAndIsSeenFalse(
            String conversationId,
            String userId
    );


    Message findTopByConversationIdOrderByCreatedAtDesc(
            String conversationId
    );

}