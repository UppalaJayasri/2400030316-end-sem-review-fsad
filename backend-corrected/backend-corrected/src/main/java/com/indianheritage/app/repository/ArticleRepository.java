package com.indianheritage.app.repository;

import com.indianheritage.app.entity.Article;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByAuthorNameOrderByCreatedAtDesc(String authorName);
    List<Article> findAllByOrderByCreatedAtDesc();
    List<Article> findByStatusOrderByCreatedAtDesc(Article.ArticleStatus status);
}
