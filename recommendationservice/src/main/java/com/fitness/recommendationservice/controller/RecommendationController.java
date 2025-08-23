package com.fitness.recommendationservice.controller;

import com.fitness.recommendationservice.dto.RecommendationResponse;
import com.fitness.recommendationservice.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService service;

    @GetMapping("/v1/users/{userId}")
    public Mono<ResponseEntity<List<RecommendationResponse>>> getByUserId(@PathVariable String userId) {
        return service.getByUserId(userId)
                .collectList()
                .map(ResponseEntity::ok);
    }


    @GetMapping("/v1/activities/{activityId}")
    public Mono<ResponseEntity<RecommendationResponse>> getByActivityId(@PathVariable String activityId) {
        return service.getByActivityId(activityId)
                .map(ResponseEntity::ok);
    }

}
