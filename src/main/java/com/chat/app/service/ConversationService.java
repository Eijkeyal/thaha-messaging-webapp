package com.chat.app.service;


import com.chat.app.dto.ConversationResponse;
import com.chat.app.entity.Conversation;
import com.chat.app.entity.ConversationParticipant;
import com.chat.app.entity.User;
import com.chat.app.repository.ConversationParticipantRepository;
import com.chat.app.repository.ConversationRepository;
import com.chat.app.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;



@Service
public class ConversationService {


    private final ConversationRepository conversationRepository;

    private final ConversationParticipantRepository conversationParticipantRepository;

    private final UserRepository userRepository;



    public ConversationService(
            ConversationRepository conversationRepository,
            ConversationParticipantRepository conversationParticipantRepository,
            UserRepository userRepository
    ){

        this.conversationRepository = conversationRepository;

        this.conversationParticipantRepository =
                conversationParticipantRepository;

        this.userRepository = userRepository;

    }






    public Conversation createOrGetDirectConversation(
            String targetUserId
    ){


        String currentUserId =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();





        List<ConversationParticipant> myChats =
                conversationParticipantRepository
                        .findByUserId(currentUserId);






        for(ConversationParticipant p : myChats){


            ConversationParticipant existing =
                    conversationParticipantRepository
                            .findByConversationIdAndUserId(
                                    p.getConversationId(),
                                    targetUserId
                            );



            if(existing != null){


                return conversationRepository
                        .findById(
                                p.getConversationId()
                        )
                        .orElseThrow();


            }


        }






        User targetUser =
                userRepository
                        .findById(targetUserId)
                        .orElseThrow();






        Conversation conversation =
                new Conversation();



        conversation.setConversationId(
                UUID.randomUUID().toString()
        );



        conversation.setConversationType(
                "DIRECT"
        );



        conversation.setTitle(
                targetUser.getUsername()
        );



        conversation.setCreatedAt(
                LocalDateTime.now()
        );



        conversationRepository.save(conversation);







        ConversationParticipant me =
                new ConversationParticipant();



        me.setParticipantId(
                UUID.randomUUID().toString()
        );


        me.setConversationId(
                conversation.getConversationId()
        );


        me.setUserId(
                currentUserId
        );


        conversationParticipantRepository.save(me);







        ConversationParticipant target =
                new ConversationParticipant();



        target.setParticipantId(
                UUID.randomUUID().toString()
        );


        target.setConversationId(
                conversation.getConversationId()
        );


        target.setUserId(
                targetUserId
        );


        conversationParticipantRepository.save(target);






        return conversation;


    }
    public List<ConversationResponse> getConversations(){


        String currentUserId =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();



        List<ConversationParticipant> myChats =
                conversationParticipantRepository
                        .findByUserId(currentUserId);



        return myChats.stream()

                .map(participant -> {


                    Conversation conversation =
                            conversationRepository
                                    .findById(
                                            participant.getConversationId()
                                    )
                                    .orElseThrow();



                    List<ConversationParticipant> members =
                            conversationParticipantRepository
                                    .findByConversationId(
                                            conversation.getConversationId()
                                    );



                    ConversationParticipant otherParticipant =
                            members.stream()

                                    .filter(p ->
                                            !p.getUserId()
                                                    .equals(currentUserId)
                                    )

                                    .findFirst()

                                    .orElseThrow();



                    User otherUser =
                            userRepository

                                    .findById(
                                            otherParticipant.getUserId()
                                    )

                                    .orElseThrow();





                    return new ConversationResponse(

                            conversation.getConversationId(),

                            otherUser.getUserId(),

                            otherUser.getUsername(),

                            null,

                            null

                    );


                })

                .toList();


    }
}