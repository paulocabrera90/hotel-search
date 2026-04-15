package com.pcabrera.hotel_search.application.service;

import com.pcabrera.hotel_search.application.service.CountSearchService;
import com.pcabrera.hotel_search.domain.model.SearchResultDomain;
import com.pcabrera.hotel_search.infrastructure.adapter.in.domain.CountResponseDomain;
import com.pcabrera.hotel_search.infrastructure.adapter.out.persistence.model.SearchDbModel;
import com.pcabrera.hotel_search.infrastructure.adapter.out.persistence.repository.SearchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CountSearchServiceTest {

    private SearchRepository repository;
    private CountSearchService service;

    @BeforeEach
    void setUp() {
        repository = mock(SearchRepository.class);
        service = new CountSearchService(repository);
    }

    @Test
    @DisplayName("Debe retornar el conteo cuando el searchId existe")
    void shouldReturnCountWhenSearchIdExists() {
        SearchDbModel entity = SearchDbModel.builder()
                .searchId("search-123")
                .hotelId("hotel-123")
                .checkIn(LocalDate.of(2026, 4, 10))
                .checkOut(LocalDate.of(2026, 4, 15))
                .ages(List.of(20, 35))
                .agesSignature("20,35")
                .build();

        when(repository.findById("search-123"))
                .thenReturn(Optional.of(entity));

        when(repository.countEqualsSearch(
                entity.getHotelId(),
                entity.getCheckIn(),
                entity.getCheckOut(),
                entity.getAgesSignature()
        )).thenReturn(5L);

        CountResponseDomain response = service.countBySearchId("search-123");

        assertNotNull(response);
        assertEquals("search-123", response.searchId());
        assertEquals(5L, response.count());

        SearchResultDomain search = response.search();
        assertNotNull(search);
        assertEquals("hotel-123", search.hotelId());
        assertEquals(LocalDate.of(2026, 4, 10), search.checkIn());
        assertEquals(LocalDate.of(2026, 4, 15), search.checkOut());
        assertEquals(List.of(20, 35), search.ages());

        verify(repository, times(1)).findById("search-123");
        verify(repository, times(1)).countEqualsSearch(
                entity.getHotelId(),
                entity.getCheckIn(),
                entity.getCheckOut(),
                entity.getAgesSignature()
        );
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el searchId no existe")
    void shouldThrowExceptionWhenSearchIdDoesNotExist() {
        when(repository.findById("search-123"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.countBySearchId("search-123")
        );

        assertEquals("searchId no encontrado: search-123", exception.getMessage());

        verify(repository, times(1)).findById("search-123");
        verify(repository, never()).countEqualsSearch(anyString(), any(), any(), anyString());
    }
}