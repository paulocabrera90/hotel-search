package com.pcabrera.hotel_search.application.service;

import com.pcabrera.hotel_search.application.service.SearchService;
import com.pcabrera.hotel_search.domain.model.SearchMessage;
import com.pcabrera.hotel_search.infrastructure.adapter.in.domain.SearchRequestDomain;
import com.pcabrera.hotel_search.infrastructure.adapter.in.domain.SearchResponseDomain;
import com.pcabrera.hotel_search.infrastructure.adapter.out.kafka.SearchKafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchServiceTest {

    private SearchKafkaProducer searchKafkaProducer;
    private SearchService searchService;

    @BeforeEach
    void setUp() {
        searchKafkaProducer = mock(SearchKafkaProducer.class);
        searchService = new SearchService(searchKafkaProducer);
    }

    @Test
    @DisplayName("Debe crear una búsqueda y enviarla al producer")
    void shouldCreateSearchAndSendMessage() {
        SearchRequestDomain request = new SearchRequestDomain(
                "hotel-123",
                LocalDate.of(2026, 4, 10),
                LocalDate.of(2026, 4, 15),
                List.of(20, 35)
        );

        ArgumentCaptor<SearchMessage> messageCaptor = ArgumentCaptor.forClass(SearchMessage.class);

        SearchResponseDomain response = searchService.createSearch(request);

        assertNotNull(response);
        assertNotNull(response.searchId());
        assertDoesNotThrow(() -> UUID.fromString(response.searchId()));

        verify(searchKafkaProducer, times(1)).send(messageCaptor.capture());

        SearchMessage capturedMessage = messageCaptor.getValue();
        assertNotNull(capturedMessage);
        assertEquals(response.searchId(), capturedMessage.searchId());
        assertEquals(request.hotelId(), capturedMessage.hotelId());
        assertEquals(request.checkIn(), capturedMessage.checkIn());
        assertEquals(request.checkOut(), capturedMessage.checkOut());
        assertEquals(request.ages(), capturedMessage.ages());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando checkIn no es anterior a checkOut")
    void shouldThrowExceptionWhenCheckInIsNotBeforeCheckOut() {
        SearchRequestDomain request = new SearchRequestDomain(
                "hotel-123",
                LocalDate.of(2026, 4, 15),
                LocalDate.of(2026, 4, 10),
                List.of(20, 35)
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> searchService.createSearch(request)
        );

        assertEquals("checkIn must be before checkOut", exception.getMessage());
        verify(searchKafkaProducer, never()).send(any(SearchMessage.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando checkIn es igual a checkOut")
    void shouldThrowExceptionWhenCheckInEqualsCheckOut() {
        SearchRequestDomain request = new SearchRequestDomain(
                "hotel-123",
                LocalDate.of(2026, 4, 10),
                LocalDate.of(2026, 4, 10),
                List.of(20, 35)
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> searchService.createSearch(request)
        );

        assertEquals("checkIn must be before checkOut", exception.getMessage());
        verify(searchKafkaProducer, never()).send(any(SearchMessage.class));
    }
}