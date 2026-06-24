package com.chat.app.controller;
import com.chat.app.dto.MessageResponse;
import com.chat.app.entity.Message;
import com.chat.app.repository.ConversationParticipantRepository;
import com.chat.app.repository.MessageRepository;
import com.chat.app.repository.UserRepository;
import com.chat.app.service.MessageService;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ConversationParticipantRepository participantRepository;
    private final MessageService messageService;
    private final UserRepository userRepository;

    public MessageController(
            MessageRepository messageRepository,
            SimpMessagingTemplate messagingTemplate,
            ConversationParticipantRepository participantRepository,
            MessageService messageService,
            UserRepository userRepository
    ) {

        this.messageRepository = messageRepository;

        this.messagingTemplate = messagingTemplate;

        this.participantRepository = participantRepository;

        this.messageService = messageService;

        this.userRepository = userRepository;

    }

    @PostMapping
    public Message sendMessage(
            @RequestBody Message message
    ) {
        String userId =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();
        boolean member =
                participantRepository
                        .existsByConversationIdAndUserId(
                                message.getConversationId(),
                                userId
                        );
        if (!member) {

            throw new RuntimeException(
                    "You are not participant"
            );

        }

        message.setMessageId(
                UUID.randomUUID().toString()
        );
        message.setSenderId(userId);
        message.setIsDelivered(false);
        message.setIsSeen(false);
        if (message.getReplyToMessageId() != null) {
            Message oldMessage =
                    messageRepository
                            .findById(
                                    message.getReplyToMessageId()
                            )
                            .orElse(null);


            if (oldMessage != null) {

                message.setReplyToContent(
                        oldMessage.getContent()
                );

            }


        }


        message.setCreatedAt(
                LocalDateTime.now()
        );


        Message saved =
                messageRepository.save(message);
        String username =
                userRepository
                        .findById(saved.getSenderId())
                        .map(user -> user.getUsername())
                        .orElse("User");

        MessageResponse response =
                new MessageResponse(

                        saved.getMessageId(),

                        saved.getSenderId(),

                        username,

                        saved.getContent(),

                        saved.getCreatedAt()

                );


        messagingTemplate.convertAndSend(

                "/topic/conversation/"
                        + saved.getConversationId(),

                response

        );
        return saved;
    }

    @GetMapping("/conversation/{conversationId}")
    public List<MessageResponse> getMessages(
            @PathVariable String conversationId
    ) {
        String userId =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();


        boolean member =
                participantRepository
                        .existsByConversationIdAndUserId(
                                conversationId,
                                userId
                        );


        if (!member) {

            throw new RuntimeException(
                    "Not participant"
            );

        }


        List<Message> messages =
                messageRepository
                        .findByConversationIdOrderByCreatedAtAsc(
                                conversationId
                        );


        List<MessageResponse> response =
                new ArrayList<>();


        for (Message message : messages) {


            String username =
                    userRepository
                            .findById(message.getSenderId())
                            .map(user -> user.getUsername())
                            .orElse("User");


            response.add(

                    new MessageResponse(

                            message.getMessageId(),

                            message.getSenderId(),

                            username,

                            message.getContent(),

                            message.getCreatedAt()

                    )

            );


        }


        return response;

    }
    @DeleteMapping("/{messageId}")
    public Message deleteMessage(
            @PathVariable String messageId
    ) {
        String userId =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();
        Message message =
                messageRepository
                        .findById(messageId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Message not found"
                                ));
        if (!message.getSenderId()
                .equals(userId)) {


            throw new RuntimeException(
                    "Only owner can delete"
            );

        }
        message.setIsDeleted(true);


        Message saved =
                messageRepository.save(message);

        String username =
                userRepository
                        .findById(saved.getSenderId())
                        .map(user -> user.getUsername())
                        .orElse("User");


        MessageResponse response =
                new MessageResponse(

                        saved.getMessageId(),
                        saved.getSenderId(),
                        username,
                        saved.getContent(),
                        saved.getCreatedAt()

                );

        messagingTemplate.convertAndSend(

                "/topic/conversation/"
                        + saved.getConversationId(),

                response

        );


        return saved;


    }
    @PutMapping("/{messageId}")
    public Message editMessage(
            @PathVariable String messageId,
            @RequestBody Message request
    ) {
        String userId =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();
        Message message =
                messageRepository
                        .findById(messageId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Message not found"
                                ));
        if (!message.getSenderId()
                .equals(userId)) {


            throw new RuntimeException(
                    "Only owner can edit"
            );

        }
        message.setContent(
                request.getContent()
        );

        message.setIsEdited(true);
        message.setEditedAt(
                LocalDateTime.now()
        );


        Message saved =
                messageRepository.save(message);


        messagingTemplate.convertAndSend(

                "/topic/conversation/"
                        + saved.getConversationId(),

                saved

        );


        return saved;


    }

    @PutMapping("/{messageId}/delivered")
    public ResponseEntity<?> markDelivered(

            @PathVariable String messageId

    ) {

        Message message =
                messageService.markDelivered(messageId);


        messagingTemplate.convertAndSend(

                "/topic/conversation/"
                        + message.getConversationId(),

                message

        );


        return ResponseEntity.ok(
                "Delivered"
        );


    }

    @PutMapping("/seen/{conversationId}")
    public ResponseEntity<?> markSeen(
            @PathVariable String conversationId
    ) {

        String userId =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();


        List<Message> messages =
                messageRepository.findByConversationId(conversationId);


        for (Message message : messages) {


            // only other user's messages
            if (!message.getSenderId().equals(userId)) {
                message.setIsDelivered(true);
                message.setIsSeen(true);
                message.setSeenAt(LocalDateTime.now());
                messageRepository.save(message);
                messagingTemplate.convertAndSend(
                        "/topic/conversation/" + conversationId,
                        message
                );
            }

        }


        return ResponseEntity.ok("Messages marked seen");

    }
}