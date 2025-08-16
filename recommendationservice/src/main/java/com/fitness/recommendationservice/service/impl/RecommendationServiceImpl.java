package com.fitness.recommendationservice.service.impl;

import com.fitness.recommendationservice.dto.RecommendationResponse;
import com.fitness.recommendationservice.mapper.RecommendationMapper;
import com.fitness.recommendationservice.repository.RecommendationRepository;
import com.fitness.recommendationservice.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationRepository recommendationRepository;

    @Override
    public Flux<RecommendationResponse> getByUserId(String userId) {
        return recommendationRepository.findByUserId(userId)
                .map(RecommendationMapper::from);
    }

    @Override
    public Mono<RecommendationResponse> getByActivityId(String activityId) {
        return recommendationRepository.findByActivityId(activityId)
                .map(RecommendationMapper::from)
                .switchIfEmpty(Mono.error(new RuntimeException("Activity not found")));
    }
}
