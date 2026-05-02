package com.indianheritage.app.service;

import com.indianheritage.app.dto.ArticleResponse;
import com.indianheritage.app.dto.ArticleSubmitRequest;
import com.indianheritage.app.entity.Article;
import com.indianheritage.app.entity.Article.ArticleStatus;
import com.indianheritage.app.entity.User;
import com.indianheritage.app.repository.ArticleRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public ArticleResponse submitArticle(ArticleSubmitRequest request, User author) {
        Article article = new Article(
            request.getTitle().trim(),
            request.getCategory().trim(),
            request.getContent().trim(),
            author.getName()
        );
        Article saved = articleRepository.save(article);
        return new ArticleResponse(saved);
    }

    public List<ArticleResponse> getMyArticles(String authorName) {
        return articleRepository.findByAuthorNameOrderByCreatedAtDesc(authorName)
            .stream()
            .map(ArticleResponse::new)
            .collect(Collectors.toList());
    }

    public List<ArticleResponse> getAllArticles() {
        return articleRepository.findAllByOrderByCreatedAtDesc()
            .stream()
            .map(ArticleResponse::new)
            .collect(Collectors.toList());
    }

    public List<ArticleResponse> getApprovedArticles() {
        return articleRepository.findByStatusOrderByCreatedAtDesc(ArticleStatus.APPROVED)
            .stream()
            .map(ArticleResponse::new)
            .collect(Collectors.toList());
    }

    public ArticleResponse updateArticleStatus(Long articleId, ArticleStatus status) {
        Article article = articleRepository.findById(articleId)
            .orElseThrow(() -> new IllegalArgumentException("Article not found"));
        
        article.setStatus(status);
        Article saved = articleRepository.save(article);
        return new ArticleResponse(saved);
    }

    public void deleteArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
            .orElseThrow(() -> new IllegalArgumentException("Article not found"));
        articleRepository.delete(article);
    }
}
