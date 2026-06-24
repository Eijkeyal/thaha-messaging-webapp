package com.chat.app.controller;

import com.chat.app.dto.ConversationResponse;
import com.chat.app.entity.Conversation;
import com.chat.app.entity.ConversationParticipant;
import com.chat.app.entity.Message;
import com.chat.app.model.CreateConversationRequest;
import com.chat.app.repository.ConversationRepository;
import com.chat.app.repository.ConversationParticipantRepository;
import com.chat.app.repository.MessageRepository;
import com.chat.app.repository.UserRepository;
import java.util.Comparator;


import com.chat.app.service.ConversationService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {


    private final ConversationRepository conversationRepository;

    private final ConversationParticipantRepository participantRepository;

    private final MessageRepository messageRepository;

    private final UserRepository userRepository;

    private final ConversationService conversationService;

    public ConversationController(
            ConversationRepository conversationRepository,
            ConversationParticipantRepository participantRepository,
            MessageRepository messageRepository,
            UserRepository userRepository,
            ConversationService conversationService
    ){

        this.conversationRepository = conversationRepository;
        this.participantRepository = participantRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.conversationService = conversationService;

    }

    @PostMapping
    public String createConversation(
            @RequestBody Map<String,List<String>> body
    ){
        String currentUserId =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();
        String conversationId =
                UUID.randomUUID().toString();
        Conversation conversation =
                new Conversation();
        conversation.setConversationId(conversationId);

        conversation.setConversationType("GROUP");

        conversation.setTitle("Chat");

        conversationRepository.save(conversation);
        List<String> users =
                body.get("userIds");
        users.add(currentUserId);

        for(String userId : users){
            ConversationParticipant participant =
                    new ConversationParticipant();
            participant.setParticipantId(
                    UUID.randomUUID().toString()
            );

            participant.setConversationId(
                    conversationId
            );

            participant.setUserId(
                    userId
            );

            participantRepository.save(participant);

        }

        return "Conversation created: " + conversationId;
    }
    @GetMapping
    public List<ConversationResponse> getAllConversations(){

        String currentUserId =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();
        List<ConversationParticipant> participants =
                participantRepository.findByUserId(currentUserId);
        List<ConversationResponse> response =
                new ArrayList<>();

        for(ConversationParticipant participant : participants){
            Message lastMessage =
                    messageRepository
                            .findTopByConversationIdOrderByCreatedAtDesc(
                                    participant.getConversationId()
                            );
            ConversationParticipant otherUser =
                    participantRepository
                            .findByConversationIdAndUserIdNot(
                                    participant.getConversationId(),
                                    currentUserId
                            );
            String username = "User";

            String otherUserId = null;

            if(otherUser != null){
                otherUserId = otherUser.getUserId();
                username =
                        userRepository
                                .findById(otherUserId)
                                .map(user -> user.getUsername())
                                .orElse("User");
            }
            response.add(

                    new ConversationResponse(

                            participant.getConversationId(),
                            otherUserId,

                            username,


                            lastMessage != null
                                    ?
                                    lastMessage.getContent()
                                    :
                                    null,


                            lastMessage != null
                                    ?
                                    lastMessage.getCreatedAt()
                                    :
                                    null

                    )

            );


        }

        return response.stream()
                .sorted(
                        Comparator.comparing(
                                ConversationResponse::getLastMessageTime,
                                Comparator.nullsLast(Comparator.naturalOrder())
                        ).reversed()
                )
                .toList();

    }
    @PostMapping("/direct")
    public Conversation createDirectConversation(
            @RequestBody CreateConversationRequest request
    ) {

        return conversationService.createOrGetDirectConversation(
                request.getTargetUserId()
        );

    }

}