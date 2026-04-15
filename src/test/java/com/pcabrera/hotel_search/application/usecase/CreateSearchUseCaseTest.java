package com.pcabrera.hotel_search.application.usecase;
import com.pcabrera.hotel_search.application.service.SearchMessageFactoryService;
import com.pcabrera.hotel_search.application.service.SearchValidatorService;
import com.pcabrera.hotel_search.application.usecase.CreateSearchUseCase;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateSearchUseCaseTest {

    private SearchValidatorService searchValidatorService;
    private SearchMessageFactoryService searchMessageFactoryService;
    private SearchKafkaProducer searchKafkaProducer;
    private CreateSearchUseCase useCase;

    @BeforeEach
    void setUp() {
        searchValidatorService = mock(SearchValidatorService.class);
        searchMessageFactoryService = mock(SearchMessageFactoryService.class);
        searchKafkaProducer = mock(SearchKafkaProducer.class);
        useCase = new CreateSearchUseCase(
                searchValidatorService,
                searchMessageFactoryService,
                searchKafkaProducer
        );
    }

    @Test
    @DisplayName("Debe validar, crear el message, enviarlo y retornar el searchId")
    void shouldValidateCreateMessageSendAndReturnSearchId() {
        SearchRequestDomain request = new SearchRequestDomain(
                "hotel-123",
                LocalDate.of(2026, 4, 10),
                LocalDate.of(2026, 4, 15),
                List.of(20, 35)
        );

        ArgumentCaptor<String> searchIdCaptor = ArgumentCaptor.forClass(String.class);

        SearchMessage message = new SearchMessage(
                "generated-id",
                request.hotelId(),
                request.checkIn(),
                request.checkOut(),
                request.ages()
        );

        when(searchMessageFactoryService.create(any(String.class), eq(request)))
                .thenReturn(message);

        SearchResponseDomain response = useCase.execute(request);

        assertNotNull(response);
        assertNotNull(response.searchId());
        assertDoesNotThrow(() -> UUID.fromString(response.searchId()));

        verify(searchValidatorService, times(1)).validate(request);
        verify(searchMessageFactoryService, times(1))
                .create(searchIdCaptor.capture(), eq(request));
        verify(searchKafkaProducer, times(1)).send(message);

        String capturedSearchId = searchIdCaptor.getValue();
        assertNotNull(capturedSearchId);
        assertEquals(response.searchId(), capturedSearchId);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la validación falla")
    void shouldThrowExceptionWhenValidationFails() {
        SearchRequestDomain request = new SearchRequestDomain(
                "hotel-123",
                LocalDate.of(2026, 4, 15),
                LocalDate.of(2026, 4, 10),
                List.of(20, 35)
        );

        doThrow(new IllegalArgumentException("checkIn must be before checkOut"))
                .when(searchValidatorService).validate(request);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(request)
        );

        assertEquals("checkIn must be before checkOut", exception.getMessage());

        verify(searchValidatorService, times(1)).validate(request);
        verify(searchMessageFactoryService, never()).create(any(String.class), any());
        verify(searchKafkaProducer, never()).send(any(SearchMessage.class));
    }
}