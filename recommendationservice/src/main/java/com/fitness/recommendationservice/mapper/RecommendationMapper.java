package com.fitness.recommendationservice.mapper;

import com.fitness.recommendationservice.dto.RecommendationResponse;
import com.fitness.recommendationservice.model.Recommendation;

public final class RecommendationMapper {

    public static RecommendationResponse from(Recommendation recommendation) {
        return RecommendationResponse.builder()
                .id(recommendation.getId())
                .activityType(recommendation.getActivityType())
                .activityId(recommendation.getActivityId())
                .userId(recommendation.getUserId())
                .recommendation(recommendation.getRecommendation())
                .suggestions(recommendation.getSuggestions())
                .improvements(recommendation.getImprovements())
                .safety(recommendation.getSafety())
                .updatedAt(recommendation.getUpdatedAt())
                .createdAt(recommendation.getCreatedAt())
                .build();
    }
}
