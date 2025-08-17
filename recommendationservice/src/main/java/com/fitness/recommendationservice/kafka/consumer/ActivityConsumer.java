package com.fitness.recommendationservice.kafka.consumer;

import com.fitness.recommendationservice.model.Activity;
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

    @PostConstruct
    public void init() {
        template.receiveAutoAck()
                .doOnNext(record -> {
                    log.info("Received record from topic [{}], partition [{}], offset [{}] with activity id [{}]",
                            record.topic(),
                            record.partition(),
                            record.offset(),
                            record.value().getId());
                })
                .subscribe();
    }
}
