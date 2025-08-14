package com.fitness.activityservice.service.impl;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.mapper.ActivityMapper;
import com.fitness.activityservice.model.Activity;
import com.fitness.activityservice.repository.ActivityRepository;
import com.fitness.activityservice.service.ActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final WebClient userClient;


    @Override
    public ActivityResponse trackActivity(ActivityRequest request) {
        validateUserId(request.getUserId());
        Activity savedActivity = activityRepository.save(ActivityMapper.from(request));
        return ActivityMapper.from(savedActivity);
    }

    @Override
    public List<ActivityResponse> getUserActivities(String userId) {
        return activityRepository.findByUserId(userId)
                .stream()
                .map(ActivityMapper::from)
                .toList();
    }

    @Override
    public ActivityResponse findById(String id) {
        return activityRepository.findById(id)
                .map(ActivityMapper::from)
                .orElseThrow(() -> new RuntimeException("Not Found"));
    }

    public void validateUserId(String userId) {
        try {
            userClient.get()
                    .uri("/api/v1/users/{userId}/validate", userId)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (WebClientResponseException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.error("User ID not found: {}", userId);
                throw new RuntimeException("User ID not found: " + userId);
            }
            log.error("Error validating user ID: {}", userId, ex);
            throw new RuntimeException("Error while validating user ID", ex);
        }
    }
}
