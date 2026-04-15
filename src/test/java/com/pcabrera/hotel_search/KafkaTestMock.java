package com.pcabrera.hotel_search;

import com.pcabrera.hotel_search.domain.model.SearchMessage;

import java.time.LocalDate;
import java.util.List;

public class KafkaTestMock {

    public static final String HOTEL_ID = "hotel-123";
    public static final String SEARCH_ID = "search-id-123";
    public static final String INVALID_SEARCH_ID = "bad-id";

    public static final String TOPIC = "hotel-searches-topic";
    public static final String AGES_SIGNATURE = "20-35";

    public static SearchMessage validMessage() {
        return new SearchMessage(
                SEARCH_ID,
                HOTEL_ID,
                LocalDate.of(2026, 4, 10),
                LocalDate.of(2026, 4, 15),
                List.of(20, 35)
        );
    }
}
