package com.pcabrera.hotel_search.infrastructure.adapter.out.kafka;

import com.pcabrera.hotel_search.application.service.AgeSignatureService;
import com.pcabrera.hotel_search.domain.model.SearchMessage;
import com.pcabrera.hotel_search.infrastructure.adapter.out.persistence.repository.SearchRepository;
import com.pcabrera.hotel_search.infrastructure.adapter.out.persistence.model.SearchDbModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchKafkaConsumer {

    private final SearchRepository repository;
    private final AgeSignatureService ageSignatureService;

    @KafkaListener(
            topics = "${app.kafka.topic.hotel-searches}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(SearchMessage message) {

        log.info("Consumiendo mensaje searchId={}", message.searchId());

        SearchDbModel entity = SearchDbModel.builder()
                .searchId(message.searchId())
                .hotelId(message.hotelId())
                .checkIn(message.checkIn())
                .checkOut(message.checkOut())
                .ages(message.ages())
                .agesSignature(ageSignatureService.build(message.ages()))
                .build();

        repository.save(entity);

        log.info("Search guardado en DB: {}", entity.getSearchId());
    }
}