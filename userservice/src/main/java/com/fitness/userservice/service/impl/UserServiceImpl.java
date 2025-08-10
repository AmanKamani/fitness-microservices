package com.fitness.userservice.service.impl;

import com.fitness.userservice.dto.UserRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.entity.User;
import com.fitness.userservice.exception.BaseException;
import com.fitness.userservice.repository.UserRepository;
import com.fitness.userservice.service.UserService;
import com.fitness.userservice.util.ApplicationError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserResponse register(UserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw BaseException.build(ApplicationError.USER_EXISTS_WITH_EMAIL);
        }
        User savedUser = userRepository.save(request.toEntity());
        log.info("User saved with id {}", savedUser.getId());
        return savedUser.toDto();
    }

    @Override
    public UserResponse findById(UUID id) {
        return userRepository
                .findById(id)
                .map(User::toDto)
                .orElseThrow(
                        () -> {
                            log.error("User not found with id {}", id);
                            return BaseException.build(ApplicationError.USER_NOT_FOUND);
                        });
    }
}
