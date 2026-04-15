package com.pcabrera.hotel_search.application.usecase;

import com.pcabrera.hotel_search.application.port.in.CreateSearchPortIn;
import com.pcabrera.hotel_search.application.service.SearchMessageFactoryService;
import com.pcabrera.hotel_search.application.service.SearchValidatorService;
import com.pcabrera.hotel_search.domain.model.SearchMessage;
import com.pcabrera.hotel_search.infrastructure.adapter.in.domain.SearchRequestDomain;
import com.pcabrera.hotel_search.infrastructure.adapter.in.domain.SearchResponseDomain;
import com.pcabrera.hotel_search.infrastructure.adapter.out.kafka.SearchKafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateSearchUseCase implements CreateSearchPortIn {

    private final SearchValidatorService searchValidatorService;
    private final SearchMessageFactoryService searchMessageFactoryService;
    private final SearchKafkaProducer searchKafkaProducer;
    @Override
    public SearchResponseDomain execute(SearchRequestDomain request) {
        searchValidatorService.validate(request);

        String searchId = UUID.randomUUID().toString();
        SearchMessage message = searchMessageFactoryService.create(searchId, request);

        searchKafkaProducer.send(message);

        return new SearchResponseDomain(searchId);
    }
}