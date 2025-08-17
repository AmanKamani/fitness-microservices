package com.fitness.recommendationservice.service.impl;

import com.fitness.recommendationservice.service.AIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiAIService implements AIService {
    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiUrl;

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String API_KEY_HEADER = "X-goog-api-key";

    @Override
    public Mono<String> getAnswer(String prompt) {
        Map<String, Object[]> request = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );
        return webClient
                .post()
                .uri(geminiUrl)
                .header(API_KEY_HEADER, apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class);
    }
}
