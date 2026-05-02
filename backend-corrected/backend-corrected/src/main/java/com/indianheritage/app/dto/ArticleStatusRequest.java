package com.indianheritage.app.dto;

import com.indianheritage.app.entity.Article.ArticleStatus;
import jakarta.validation.constraints.NotNull;

public class ArticleStatusRequest {

    @NotNull(message = "Status is required")
    private ArticleStatus status;

    public ArticleStatus getStatus() { return status; }
    public void setStatus(ArticleStatus status) { this.status = status; }
}
