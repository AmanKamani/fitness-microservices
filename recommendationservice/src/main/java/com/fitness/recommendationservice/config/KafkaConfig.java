package com.fitness.recommendationservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.recommendationservice.model.Activity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;

@Configuration
public class KafkaConfig {

    @Bean
    public ReactiveKafkaConsumerTemplate<String, Activity> activityConsumerTemplate(
            KafkaProperties kafkaProperties,
            ObjectMapper objectMapper,
            @Value("${spring.kafka.topics.activity.name}") String activityTopic) {

        JsonDeserializer<Activity> deserializer = new JsonDeserializer<>(Activity.class, objectMapper, false);
//        deserializer.addTrustedPackages("com.fitness.recommendationservice.model.Activity");

        ReceiverOptions<String, Activity> options = ReceiverOptions
                .<String, Activity>create(kafkaProperties.buildConsumerProperties())
                .withValueDeserializer(deserializer)
                .subscription(Collections.singletonList(activityTopic));

        return new ReactiveKafkaConsumerTemplate<>(options);
    }
}
