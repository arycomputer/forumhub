package br.com.desafioalura.forumhub.web;

// src/main/java/com/example/demo/web/CreatePostRequest.java

import java.io.Serializable;

public class CreatePostRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private String content;
    private Long courseId; // Novo campo para vincular o curso

    public CreatePostRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCourseId() { // Getter para courseId
        return courseId;
    }

    public void setCourseId(Long courseId) { // Setter para courseId
        this.courseId = courseId;
    }
}
