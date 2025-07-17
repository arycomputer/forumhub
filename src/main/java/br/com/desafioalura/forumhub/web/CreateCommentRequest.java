package br.com.desafioalura.forumhub.web;

// src/main/java/com/example/demo/web/CreateCommentRequest.java

import java.io.Serializable;

public class CreateCommentRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String content;

    public CreateCommentRequest() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}