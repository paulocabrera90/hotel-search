package com.pcabrera.hotel_search.application.service;

import com.pcabrera.hotel_search.domain.model.SearchMessage;
import com.pcabrera.hotel_search.infrastructure.adapter.in.domain.SearchRequestDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchMessageFactoryServiceTest {

    private SearchMessageFactoryService service;

    @BeforeEach
    void setUp() {
        service = new SearchMessageFactoryService();
    }

    @Test
    @DisplayName("Debe crear correctamente un SearchMessage a partir del request")
    void shouldCreateSearchMessage() {
        String searchId = "search-123";

        SearchRequestDomain request = new SearchRequestDomain(
                "hotel-123",
                LocalDate.of(2026, 4, 10),
                LocalDate.of(2026, 4, 15),
                List.of(20, 35)
        );

        SearchMessage result = service.create(searchId, request);

        assertNotNull(result);
        assertEquals(searchId, result.searchId());
        assertEquals(request.hotelId(), result.hotelId());
        assertEquals(request.checkIn(), result.checkIn());
        assertEquals(request.checkOut(), result.checkOut());
        assertEquals(request.ages(), result.ages());
    }
}