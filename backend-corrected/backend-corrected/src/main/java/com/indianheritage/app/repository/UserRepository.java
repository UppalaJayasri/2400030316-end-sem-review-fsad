package com.indianheritage.app.repository;

import com.indianheritage.app.entity.User;
import com.indianheritage.app.entity.UserRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    long countByRoleNot(UserRole role);
    
    List<User> findByRoleNot(UserRole role);
}
