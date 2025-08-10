package com.fitness.userservice.dto;

import com.fitness.userservice.entity.User;
import com.fitness.userservice.mapper.EntityMapper;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequest implements EntityMapper<User> {
    @Email
    @NotNull
    private String email;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

    @NotBlank
    @Size(max = 50)
    private String name;

    @Override
    public User toEntity() {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        return user;
    }
}
