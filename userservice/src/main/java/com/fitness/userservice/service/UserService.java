package com.fitness.userservice.service;

import com.fitness.userservice.dto.UserRequest;
import com.fitness.userservice.dto.UserResponse;

import java.util.UUID;

public interface UserService {
    UserResponse register(UserRequest request);

    UserResponse findById(UUID id);
}
