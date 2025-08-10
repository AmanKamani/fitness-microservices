package com.fitness.userservice.entity;

import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.enums.UserRole;
import com.fitness.userservice.mapper.DtoMapper;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class User implements DtoMapper<UserResponse> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    private String name;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Override
    public UserResponse toDto() {
        UserResponse response = new UserResponse();
        response.setId(id.toString());
        response.setEmail(email);
        response.setName(name);
        response.setRole(role);
        response.setCreatedAt(createdAt);
        response.setUpdatedAt(updatedAt);
        return response;
    }
}
