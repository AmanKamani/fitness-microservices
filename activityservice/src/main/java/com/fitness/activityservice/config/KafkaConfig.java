package com.fitness.activityservice.config;

import com.fitness.activityservice.model.Activity;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.sender.SenderOptions;

@Configuration
public class KafkaConfig {


    @Bean
    public NewTopic activityTopic(
            @Value("${spring.kafka.topics.activity.name}") String topicName,
            @Value("${spring.kafka.topics.activity.partitions:1}") int partitions
    ) {
        return TopicBuilder
                .name(topicName)
                .partitions(partitions)
                .build();
    }

    @Bean
    public ReactiveKafkaProducerTemplate<String, Activity> activityKafkaProducerTemplate(KafkaProperties kafkaProperties) {
        return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(kafkaProperties.buildProducerProperties()));
    }
}
