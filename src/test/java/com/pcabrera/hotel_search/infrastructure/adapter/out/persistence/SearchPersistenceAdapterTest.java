package com.pcabrera.hotel_search.infrastructure.adapter.out.persistence;

import com.pcabrera.hotel_search.SearchTestMock;
import com.pcabrera.hotel_search.infrastructure.adapter.out.persistence.model.SearchDbModel;
import com.pcabrera.hotel_search.infrastructure.adapter.out.persistence.repository.SearchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchPersistenceAdapterTest {

    private SearchRepository repository;
    private SearchPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        repository = mock(SearchRepository.class);
        adapter = new SearchPersistenceAdapter(repository);
    }

    @Test
    @DisplayName("Debe retornar el search cuando existe")
    void shouldReturnSearchWhenExists() {
        SearchDbModel entity = SearchDbModel.builder()
                .searchId(SearchTestMock.SEARCH_ID)
                .hotelId(SearchTestMock.HOTEL_ID)
                .build();

        when(repository.findById(SearchTestMock.SEARCH_ID))
                .thenReturn(Optional.of(entity));

        SearchDbModel result = adapter.findById(SearchTestMock.SEARCH_ID);

        assertNotNull(result);
        assertEquals(SearchTestMock.SEARCH_ID, result.getSearchId());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando searchId no existe")
    void shouldThrowExceptionWhenSearchNotFound() {
        when(repository.findById(SearchTestMock.SEARCH_ID))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> adapter.findById(SearchTestMock.SEARCH_ID)
        );

        assertTrue(exception.getMessage().contains("no encontrado"));
    }

    @Test
    @DisplayName("Debe delegar correctamente el count al repository")
    void shouldDelegateCountToRepository() {
        SearchDbModel entity = SearchDbModel.builder()
                .hotelId("hotel-123")
                .checkIn(java.time.LocalDate.of(2026, 4, 10))
                .checkOut(java.time.LocalDate.of(2026, 4, 15))
                .agesSignature("20-35")
                .build();

        when(repository.countEqualsSearch(
                entity.getHotelId(),
                entity.getCheckIn(),
                entity.getCheckOut(),
                entity.getAgesSignature()
        )).thenReturn(5L);

        long result = adapter.countBySearch(entity);

        assertEquals(5L, result);

        verify(repository, times(1)).countEqualsSearch(
                entity.getHotelId(),
                entity.getCheckIn(),
                entity.getCheckOut(),
                entity.getAgesSignature()
        );
    }
}