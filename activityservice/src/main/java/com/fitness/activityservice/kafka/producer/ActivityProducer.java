package com.fitness.activityservice.kafka.producer;

import com.fitness.activityservice.model.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActivityProducer {

    private final ReactiveKafkaProducerTemplate<String, Activity> template;
    private final NewTopic activityTopic;

    public void send(Activity activity) {
        template.send(activityTopic.name(), activity)
                .doOnError(throwable -> log.error("Error while sending activity [{}] to topic", activity.getId(), throwable))
                .doOnSuccess(result -> {
                    RecordMetadata metadata = result.recordMetadata();
                    log.info("Send activity id [{}] details to topic [{}], partition [{}], offset [{}]",
                            activity.getId(),
                            metadata.topic(),
                            metadata.partition(),
                            metadata.offset());
                })
                .subscribe();
    }
}
