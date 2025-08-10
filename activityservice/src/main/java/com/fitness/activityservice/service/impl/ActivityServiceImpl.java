package com.fitness.activityservice.service.impl;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.mapper.ActivityMapper;
import com.fitness.activityservice.model.Activity;
import com.fitness.activityservice.repository.ActivityRepository;
import com.fitness.activityservice.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;


    @Override
    public ActivityResponse trackActivity(ActivityRequest request) {

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
}
