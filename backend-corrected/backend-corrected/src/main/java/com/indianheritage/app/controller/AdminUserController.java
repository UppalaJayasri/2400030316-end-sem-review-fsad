package com.indianheritage.app.controller;

import com.indianheritage.app.dto.UserProfileResponse;
import com.indianheritage.app.entity.User;
import com.indianheritage.app.entity.UserRole;
import com.indianheritage.app.exception.ForbiddenException;
import com.indianheritage.app.exception.NotFoundException;
import com.indianheritage.app.repository.UserRepository;
import com.indianheritage.app.service.AuthService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminUserController {

    private final AuthService authService;
    private final UserRepository userRepository;

    public AdminUserController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserProfileResponse>> getUsers(
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        authService.requireAdminUser(authHeader);
        
        List<UserProfileResponse> users = userRepository.findByRoleNot(UserRole.ADMIN)
            .stream()
            .map(UserProfileResponse::new)
            .collect(Collectors.toList());
            
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(
        @PathVariable Long id,
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        authService.requireAdminUser(authHeader);

        User target = userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found"));

        if (target.getRole() == UserRole.ADMIN) {
            throw new ForbiddenException("Cannot delete admin users");
        }

        userRepository.delete(target);
        return ResponseEntity.noContent().build();
    }
}
