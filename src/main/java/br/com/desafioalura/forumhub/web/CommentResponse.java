package br.com.desafioalura.forumhub.web;

// src/main/java/com/example/demo/web/CommentResponse.java

import java.io.Serializable;
import java.time.LocalDateTime;

public class CommentResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private String userEmail; // Alterado de username para userEmail

    public CommentResponse() {
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserEmail() { // Alterado de getUsername para getUserEmail
        return userEmail;
    }

    public void setUserEmail(String userEmail) { // Alterado de setUsername para setUserEmail
        this.userEmail = userEmail;
    }
}
