package com.chat.app.repository;

import com.chat.app.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import com.chat.app.entity.User;
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    User findByEmail(String email);
}
