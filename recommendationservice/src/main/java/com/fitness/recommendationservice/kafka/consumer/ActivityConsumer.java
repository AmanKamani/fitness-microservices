package com.fitness.recommendationservice.kafka.consumer;

import com.fitness.recommendationservice.model.Activity;
import com.fitness.recommendationservice.service.RecommendationService;
import com.fitness.recommendationservice.service.impl.ActivityRecommendationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActivityConsumer {

    private final ReactiveKafkaConsumerTemplate<String, Activity> template;
    private final ActivityRecommendationService aiService;
    private final RecommendationService recommendationService;

    @PostConstruct
    public void init() {
        template.receiveAutoAck()
                .doOnNext(record -> {
                    log.info("Received activity from topic [{}], partition [{}], offset [{}] with activity id [{}] for processing",
                            record.topic(),
                            record.partition(),
                            record.offset(),
                            record.value().getId());
                })
                .concatMap(record -> aiService.generateRecommendation(record.value())
                        .doOnError(error -> log.error("Error generating recommendation for activity [{}], partition [{}], offset [{}]",
                                record.value().getId(),
                                record.partition(),
                                record.offset())
                        )
                        .flatMap(recommendationService::save)
                        .doOnNext(savedId -> {
                            log.info("Recommendation [{}] saved successfully for activity ID [{}]", savedId, record.value().getId());
                        }))
                .subscribe();
    }
}
