package com.indianheritage.app.controller;

import com.indianheritage.app.dto.ArticleResponse;
import com.indianheritage.app.dto.ArticleStatusRequest;
import com.indianheritage.app.dto.ArticleSubmitRequest;
import com.indianheritage.app.entity.User;
import com.indianheritage.app.entity.UserRole;
import com.indianheritage.app.exception.ForbiddenException;
import com.indianheritage.app.service.ArticleService;
import com.indianheritage.app.service.AuthService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ArticleController {

    private final ArticleService articleService;
    private final AuthService authService;

    public ArticleController(ArticleService articleService, AuthService authService) {
        this.articleService = articleService;
        this.authService = authService;
    }

    @PostMapping("/articles")
    public ResponseEntity<ArticleResponse> submitArticle(
        @Valid @RequestBody ArticleSubmitRequest request,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        User user = authService.requireAuthenticatedUser(authHeader);
        if (user.getRole() != UserRole.CULTURAL_ENTHUSIAST) {
            throw new ForbiddenException("Only Cultural Enthusiasts can submit articles");
        }
        return ResponseEntity.ok(articleService.submitArticle(request, user));
    }

    @GetMapping("/articles/mine")
    public ResponseEntity<List<ArticleResponse>> getMyArticles(
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        User user = authService.requireAuthenticatedUser(authHeader);
        if (user.getRole() != UserRole.CULTURAL_ENTHUSIAST) {
            throw new ForbiddenException("Only Cultural Enthusiasts can access their articles");
        }
        return ResponseEntity.ok(articleService.getMyArticles(user.getName()));
    }

    @GetMapping("/articles/approved")
    public ResponseEntity<List<ArticleResponse>> getApprovedArticles(
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        authService.requireAuthenticatedUser(authHeader);
        return ResponseEntity.ok(articleService.getApprovedArticles());
    }

    @GetMapping("/admin/articles")
    public ResponseEntity<List<ArticleResponse>> getAllArticlesForAdmin(
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        authService.requireAdminUser(authHeader);
        return ResponseEntity.ok(articleService.getAllArticles());
    }

    @PutMapping("/admin/articles/{id}/status")
    public ResponseEntity<ArticleResponse> updateArticleStatus(
        @PathVariable Long id,
        @Valid @RequestBody ArticleStatusRequest request,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        authService.requireAdminUser(authHeader);
        return ResponseEntity.ok(articleService.updateArticleStatus(id, request.getStatus()));
    }

    @DeleteMapping("/admin/articles/{id}")
    public ResponseEntity<Void> deleteArticle(
        @PathVariable Long id,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        authService.requireAdminUser(authHeader);
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}
