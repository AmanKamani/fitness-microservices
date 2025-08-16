package com.fitness.recommendationservice.repository;

import com.fitness.recommendationservice.model.Recommendation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface RecommendationRepository extends ReactiveMongoRepository<Recommendation, String> {

    Flux<Recommendation> findByUserId(String userId);

    Mono<Recommendation> findByActivityId(String activityId);
}
