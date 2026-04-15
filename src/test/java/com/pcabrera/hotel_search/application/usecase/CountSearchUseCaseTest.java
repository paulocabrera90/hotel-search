package com.pcabrera.hotel_search.application.usecase;



import com.pcabrera.hotel_search.application.service.CountSearchService;
import com.pcabrera.hotel_search.domain.model.SearchResultDomain;
import com.pcabrera.hotel_search.infrastructure.adapter.in.domain.CountResponseDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CountSearchUseCaseTest {

    private CountSearchService countSearchService;
    private CountSearchUseCase useCase;

    @BeforeEach
    void setUp() {
        countSearchService = mock(CountSearchService.class);
        useCase = new CountSearchUseCase(countSearchService);
    }

    @Test
    @DisplayName("Debe delegar en el service y retornar el resultado")
    void shouldDelegateToServiceAndReturnResponse() {
        SearchResultDomain search = new SearchResultDomain(
                "hotel-123",
                LocalDate.of(2026, 4, 10),
                LocalDate.of(2026, 4, 15),
                List.of(20, 35)
        );

        CountResponseDomain expectedResponse = new CountResponseDomain(
                "search-123",
                search,
                5L
        );

        when(countSearchService.countBySearchId("search-123"))
                .thenReturn(expectedResponse);

        CountResponseDomain response = useCase.execute("search-123");

        assertNotNull(response);
        assertEquals("search-123", response.searchId());
        assertEquals(5L, response.count());
        assertNotNull(response.search());
        assertEquals("hotel-123", response.search().hotelId());

        verify(countSearchService, times(1)).countBySearchId("search-123");
    }
}