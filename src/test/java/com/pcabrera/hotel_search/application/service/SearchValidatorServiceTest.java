package com.pcabrera.hotel_search.application.service;

import com.pcabrera.hotel_search.application.service.SearchValidatorService;
import com.pcabrera.hotel_search.infrastructure.adapter.in.domain.SearchRequestDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SearchValidatorServiceTest {

    private SearchValidatorService service;

    @BeforeEach
    void setUp() {
        service = new SearchValidatorService();
    }

    @Test
    @DisplayName("No debe lanzar excepción cuando checkIn es anterior a checkOut")
    void shouldValidateSuccessfullyWhenDatesAreValid() {
        SearchRequestDomain request = new SearchRequestDomain(
                "hotel-123",
                LocalDate.of(2026, 4, 10),
                LocalDate.of(2026, 4, 15),
                List.of(20, 35)
        );

        assertDoesNotThrow(() -> service.validate(request));
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
                () -> service.validate(request)
        );

        assertEquals("checkIn must be before checkOut", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando checkIn es posterior a checkOut")
    void shouldThrowExceptionWhenCheckInIsAfterCheckOut() {
        SearchRequestDomain request = new SearchRequestDomain(
                "hotel-123",
                LocalDate.of(2026, 4, 15),
                LocalDate.of(2026, 4, 10),
                List.of(20, 35)
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.validate(request)
        );

        assertEquals("checkIn must be before checkOut", exception.getMessage());
    }
}