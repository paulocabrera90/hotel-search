package com.pcabrera.hotel_search;

import com.pcabrera.hotel_search.domain.model.SearchResultDomain;
import com.pcabrera.hotel_search.infrastructure.adapter.in.domain.CountResponseDomain;
import com.pcabrera.hotel_search.infrastructure.adapter.in.domain.SearchRequestDomain;
import com.pcabrera.hotel_search.infrastructure.adapter.in.domain.SearchResponseDomain;

import java.time.LocalDate;
import java.util.List;

public class SearchTestMock {

    public static final String HOTEL_ID = "hotel-123";
    public static final String SEARCH_ID = "search-id-123";
    public static final String INVALID_SEARCH_ID = "bad-id";

    public static SearchRequestDomain validRequest() {
        return new SearchRequestDomain(
                HOTEL_ID,
                LocalDate.of(2026, 4, 10),
                LocalDate.of(2026, 4, 15),
                List.of(20, 35)
        );
    }

    public static SearchRequestDomain invalidDateRequest() {
        return new SearchRequestDomain(
                HOTEL_ID,
                LocalDate.of(2026, 4, 15),
                LocalDate.of(2026, 4, 10),
                List.of(20, 35)
        );
    }

    public static SearchResponseDomain validResponse() {
        return new SearchResponseDomain(SEARCH_ID);
    }

    public static SearchResultDomain validSearchResult() {
        return new SearchResultDomain(
                HOTEL_ID,
                LocalDate.of(2026, 4, 10),
                LocalDate.of(2026, 4, 15),
                List.of(20, 35)
        );
    }

    public static CountResponseDomain validCountResponse() {
        return new CountResponseDomain(
                SEARCH_ID,
                validSearchResult(),
                5L
        );
    }
}
