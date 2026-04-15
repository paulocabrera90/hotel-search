package com.pcabrera.hotel_search.application.service;

import com.pcabrera.hotel_search.domain.model.SearchResultDomain;
import com.pcabrera.hotel_search.infrastructure.adapter.in.domain.CountResponseDomain;
import com.pcabrera.hotel_search.infrastructure.adapter.out.persistence.repository.SearchRepository;
import com.pcabrera.hotel_search.infrastructure.adapter.out.persistence.model.SearchDbModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CountSearchService {

    private final SearchRepository repository;

    public CountResponseDomain countBySearchId(String searchId) {
        SearchDbModel entity = repository.findById(searchId)
                .orElseThrow(() -> new IllegalArgumentException("searchId no encontrado: " + searchId));

        long count = repository.countEqualsSearch(
                entity.getHotelId(),
                entity.getCheckIn(),
                entity.getCheckOut(),
                entity.getAgesSignature()
        );

        SearchResultDomain search = new SearchResultDomain(
                entity.getHotelId(),
                entity.getCheckIn(),
                entity.getCheckOut(),
                entity.getAges()
        );

        return new CountResponseDomain(
                entity.getSearchId(),
                search,
                count
        );
    }
}