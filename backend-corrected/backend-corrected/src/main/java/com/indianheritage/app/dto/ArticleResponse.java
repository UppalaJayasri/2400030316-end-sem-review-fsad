package com.indianheritage.app.dto;

import com.indianheritage.app.entity.Article;
import java.time.LocalDateTime;

public class ArticleResponse {

    private Long id;
    private String title;
    private String category;
    private String content;
    private String status;
    private String authorName;
    private LocalDateTime createdAt;

    public ArticleResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.category = article.getCategory();
        this.content = article.getContent();
        this.status = article.getStatus().name();
        String rawAuthorName = article.getAuthorName();
        this.authorName = (rawAuthorName == null || rawAuthorName.isBlank())
            ? "Unknown Author"
            : rawAuthorName;
        this.createdAt = article.getCreatedAt();
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getContent() { return content; }
    public String getStatus() { return status; }
    public String getAuthorName() { return authorName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
