package com.fitness.recommendationservice.service;

import com.fitness.recommendationservice.dto.RecommendationResponse;
import com.fitness.recommendationservice.model.Recommendation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface RecommendationService {
    Flux<RecommendationResponse> getByUserId(String userId);

    Mono<RecommendationResponse> getByActivityId(String activityId);

    Mono<String> save(Recommendation recommendation);
}
