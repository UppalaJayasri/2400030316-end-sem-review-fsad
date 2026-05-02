package com.indianheritage.app.service;

import com.indianheritage.app.dto.AuthRequest;
import com.indianheritage.app.dto.AuthResponse;
import com.indianheritage.app.dto.SignupRequest;
import com.indianheritage.app.entity.User;
import com.indianheritage.app.entity.UserRole;
import com.indianheritage.app.exception.ForbiddenException;
import com.indianheritage.app.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import com.indianheritage.app.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse signup(SignupRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Account already exists. Please signin.");
        }

        UserRole role = parseRole(request.getRole());
        User user = new User(
            request.getName().trim(),
            normalizedEmail,
            passwordEncoder.encode(request.getPassword()),
            role
        );

        User savedUser = userRepository.save(user);

        return new AuthResponse(
            jwtService.generateToken(savedUser),
            savedUser.getName(),
            savedUser.getEmail(),
            savedUser.getRole().name(),
            "Signup successful"
        );
    }

    public AuthResponse login(AuthRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmail(normalizedEmail)
            .orElseThrow(() -> new IllegalArgumentException("Account does not exist. Please signup first."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return new AuthResponse(
            jwtService.generateToken(user),
            user.getName(),
            user.getEmail(),
            user.getRole().name(),
            "Login successful"
        );
    }

    public User requireAuthenticatedUser(String authHeader) {
        if (authHeader == null || authHeader.isBlank() || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Sign in is required to view this information");
        }

        String token = authHeader.substring(7).trim();
        if (token.isEmpty()) {
            throw new UnauthorizedException("Token is missing");
        }

        try {
            Claims claims = jwtService.parseToken(token);
            String email = claims.getSubject();

            return userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User does not exist"));
        } catch (JwtException ex) {
            throw new UnauthorizedException("Invalid or expired token");
        }
    }

    public User requireAdminUser(String authHeader) {
        User user = requireAuthenticatedUser(authHeader);
        if (user.getRole() != UserRole.ADMIN) {
            throw new ForbiddenException("Only admin users can perform this action");
        }
        return user;
    }

    private UserRole parseRole(String roleValue) {
        String normalizedRole = roleValue.trim().toUpperCase().replace('-', '_').replace(' ', '_');
        try {
            return UserRole.valueOf(normalizedRole);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid role. Use ADMIN, CULTURAL_ENTHUSIAST, EXPLORER, or TOUR_GUIDE");
        }
    }
}
