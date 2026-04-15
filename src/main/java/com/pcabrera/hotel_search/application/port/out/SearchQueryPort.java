package com.pcabrera.hotel_search.application.port.out;

import com.pcabrera.hotel_search.infrastructure.adapter.out.persistence.model.SearchDbModel;

public interface SearchQueryPort {
    SearchDbModel findById(String searchId);
    long countBySearch(SearchDbModel search);
}