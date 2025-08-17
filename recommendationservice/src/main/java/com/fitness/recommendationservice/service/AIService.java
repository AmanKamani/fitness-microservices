package com.fitness.recommendationservice.service;

import reactor.core.publisher.Mono;

public interface AIService {
    Mono<String> getAnswer(String prompt);
}
