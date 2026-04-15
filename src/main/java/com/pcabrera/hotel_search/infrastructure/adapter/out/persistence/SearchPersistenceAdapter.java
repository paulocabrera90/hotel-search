package com.pcabrera.hotel_search.infrastructure.adapter.out.persistence;

import com.pcabrera.hotel_search.application.port.out.SearchQueryPort;
import com.pcabrera.hotel_search.infrastructure.adapter.out.persistence.model.SearchDbModel;
import com.pcabrera.hotel_search.infrastructure.adapter.out.persistence.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchPersistenceAdapter implements SearchQueryPort {

    private final SearchRepository repository;

    @Override
    public SearchDbModel findById(String searchId) {
        return repository.findById(searchId)
                .orElseThrow(() -> new IllegalArgumentException("searchId no encontrado: " + searchId));
    }

    @Override
    public long countBySearch(SearchDbModel search) {
        return repository.countEqualsSearch(
                search.getHotelId(),
                search.getCheckIn(),
                search.getCheckOut(),
                search.getAgesSignature()
        );
    }
}