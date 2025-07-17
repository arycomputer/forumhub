package br.com.desafioalura.forumhub.web;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


public class PostResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private String userEmail;
    private int likesCount;
    private Long courseId; // Novo campo para o ID do curso
    private String courseName; // Novo campo para o nome do curso
    private List<CommentResponse> comments;

    public PostResponse() {
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public Long getCourseId() { // Getter para courseId
        return courseId;
    }

    public void setCourseId(Long courseId) { // Setter para courseId
        this.courseId = courseId;
    }

    public String getCourseName() { // Getter para courseName
        return courseName;
    }

    public void setCourseName(String courseName) { // Setter para courseName
        this.courseName = courseName;
    }

    public List<CommentResponse> getComments() {
        return comments;
    }

    public void setComments(List<CommentResponse> comments) {
        this.comments = comments;
    }
}