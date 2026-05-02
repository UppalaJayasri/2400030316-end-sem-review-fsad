package com.indianheritage.app.dto;

import com.indianheritage.app.entity.User;
import com.indianheritage.app.entity.UserRole;

public class UserProfileResponse {
    private Long id;
    private String name;
    private String email;
    private UserRole role;

    public UserProfileResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public UserRole getRole() { return role; }
}
