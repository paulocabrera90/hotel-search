package com.pcabrera.hotel_search.application.service;



import com.pcabrera.hotel_search.domain.model.SearchMessage;
import com.pcabrera.hotel_search.infrastructure.adapter.in.domain.SearchRequestDomain;
import com.pcabrera.hotel_search.infrastructure.adapter.in.domain.SearchResponseDomain;
import com.pcabrera.hotel_search.infrastructure.adapter.out.kafka.SearchKafkaProducer;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SearchService {

    private final SearchKafkaProducer searchKafkaProducer;

    public SearchService(SearchKafkaProducer searchKafkaProducer) {
        this.searchKafkaProducer = searchKafkaProducer;
    }

    public SearchResponseDomain createSearch(SearchRequestDomain request) {
        validateDates(request);

        String searchId = UUID.randomUUID().toString();

        SearchMessage message = new SearchMessage(
                searchId,
                request.hotelId(),
                request.checkIn(),
                request.checkOut(),
                request.ages()
        );

        searchKafkaProducer.send(message);

        return new SearchResponseDomain(searchId);
    }

    private void validateDates(SearchRequestDomain request) {
        if (!request.checkIn().isBefore(request.checkOut())) {
            throw new IllegalArgumentException("checkIn must be before checkOut");
        }
    }
}