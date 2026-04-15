package com.pcabrera.hotel_search.infrastructure.adapter.in.domain;

import com.pcabrera.hotel_search.domain.model.SearchResultDomain;

public record CountResponseDomain(
        String searchId,
        SearchResultDomain search,
        long count
) {
}
