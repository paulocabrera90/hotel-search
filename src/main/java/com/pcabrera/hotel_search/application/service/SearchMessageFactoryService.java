package com.pcabrera.hotel_search.application.service;

import com.pcabrera.hotel_search.domain.model.SearchMessage;
import com.pcabrera.hotel_search.infrastructure.adapter.in.domain.SearchRequestDomain;
import org.springframework.stereotype.Component;

@Component
public class SearchMessageFactoryService {

    public SearchMessage create(String searchId, SearchRequestDomain request) {
        return new SearchMessage(
                searchId,
                request.hotelId(),
                request.checkIn(),
                request.checkOut(),
                request.ages()
        );
    }
}