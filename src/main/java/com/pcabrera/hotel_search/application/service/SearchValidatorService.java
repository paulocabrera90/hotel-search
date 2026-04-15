package com.pcabrera.hotel_search.application.service;

import com.pcabrera.hotel_search.infrastructure.adapter.in.domain.SearchRequestDomain;
import org.springframework.stereotype.Component;

@Component
public class SearchValidatorService {

    public void validate(SearchRequestDomain request) {
        if (!request.checkIn().isBefore(request.checkOut())) {
            throw new IllegalArgumentException("Check in must be before check out");
        }
    }
}