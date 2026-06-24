package com.chat.app.dto;

import lombok.Setter;

public class RegisterRequest {
    @Setter
    private String username;
    private String email;
    private String password;

    public String getUsername(){
        return username;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(){
        this.email = email;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(){
        this.password = password;
    }
}
