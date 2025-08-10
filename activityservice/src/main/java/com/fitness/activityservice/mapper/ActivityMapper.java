package com.fitness.activityservice.mapper;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;

public final class ActivityMapper {

    public static Activity from(ActivityRequest request) {
        return Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .startTime(request.getStartTime())
                .caloriesBurned(request.getCaloriesBurned())
                .duration(request.getDuration())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();
    }

    public static ActivityResponse from(Activity activity) {
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUserId());
        response.setType(activity.getType());
        response.setStartTime(activity.getStartTime());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setDuration(activity.getDuration());
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());
        return response;
    }
}
