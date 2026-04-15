package com.pcabrera.hotel_search.infrastructure.adapter.out.kafka;

import com.pcabrera.hotel_search.application.port.out.SearchMessagePublisherPort;
import com.pcabrera.hotel_search.domain.model.SearchMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SearchKafkaProducer implements SearchMessagePublisherPort {

    private final KafkaTemplate<String, SearchMessage> kafkaTemplate;
    private final String topic;

    public SearchKafkaProducer(
            KafkaTemplate<String, SearchMessage> kafkaTemplate,
            @Value("${app.kafka.topic.hotel-searches}") String topic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }
    @Override
    public void send(SearchMessage message) {
        kafkaTemplate.send(topic, message.searchId(), message);
    }
}
