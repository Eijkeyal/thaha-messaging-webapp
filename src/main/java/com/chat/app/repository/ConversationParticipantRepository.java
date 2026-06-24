package com.chat.app.repository;

import com.chat.app.entity.ConversationParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationParticipantRepository
        extends JpaRepository<ConversationParticipant, String> {


    boolean existsByConversationIdAndUserId(
            String conversationId,
            String userId
    );


    List<ConversationParticipant> findByUserId(
            String userId
    );


    ConversationParticipant findByConversationIdAndUserIdNot(
            String conversationId,
            String userId
    );
    ConversationParticipant findByConversationIdAndUserId(
            String conversationId,
            String userId
    );
    List<ConversationParticipant> findByConversationId(String conversationId);
}