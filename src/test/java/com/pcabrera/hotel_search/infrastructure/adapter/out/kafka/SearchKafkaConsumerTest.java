package com.pcabrera.hotel_search.infrastructure.adapter.out.kafka;

import com.pcabrera.hotel_search.KafkaTestMock;
import com.pcabrera.hotel_search.SearchTestMock;
import com.pcabrera.hotel_search.application.service.AgeSignatureService;
import com.pcabrera.hotel_search.domain.model.SearchMessage;
import com.pcabrera.hotel_search.infrastructure.adapter.out.kafka.SearchKafkaConsumer;
import com.pcabrera.hotel_search.infrastructure.adapter.out.persistence.model.SearchDbModel;
import com.pcabrera.hotel_search.infrastructure.adapter.out.persistence.repository.SearchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static com.pcabrera.hotel_search.KafkaTestMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SearchKafkaConsumerTest {

    private SearchRepository repository;
    private AgeSignatureService ageSignatureService;
    private SearchKafkaConsumer consumer;

    @BeforeEach
    void setUp() {
        repository = mock(SearchRepository.class);
        ageSignatureService = mock(AgeSignatureService.class);
        consumer = new SearchKafkaConsumer(repository, ageSignatureService);
    }

    @Test
    @DisplayName("Debe construir la signature de edades y guardar la búsqueda en DB")
    void shouldBuildAgeSignatureAndSaveSearchInDatabase() {
        SearchMessage message = validMessage();

        when(ageSignatureService.build(message.ages()))
                .thenReturn(AGES_SIGNATURE);

        consumer.consume(message);

        verify(ageSignatureService, times(1)).build(message.ages());

        ArgumentCaptor<SearchDbModel> entityCaptor = ArgumentCaptor.forClass(SearchDbModel.class);
        verify(repository, times(1)).save(entityCaptor.capture());

        SearchDbModel savedEntity = entityCaptor.getValue();

        assertNotNull(savedEntity);
        assertEquals(message.searchId(), savedEntity.getSearchId());
        assertEquals(message.hotelId(), savedEntity.getHotelId());
        assertEquals(message.checkIn(), savedEntity.getCheckIn());
        assertEquals(message.checkOut(), savedEntity.getCheckOut());
        assertEquals(message.ages(), savedEntity.getAges());
        assertEquals(AGES_SIGNATURE, savedEntity.getAgesSignature());
    }

    @Test
    @DisplayName("Debe propagar excepción cuando falla el guardado")
    void shouldThrowExceptionWhenRepositorySaveFails() {
        SearchMessage message = KafkaTestMock.validMessage();

        when(ageSignatureService.build(message.ages()))
                .thenReturn(KafkaTestMock.AGES_SIGNATURE);

        when(repository.save(org.mockito.ArgumentMatchers.any(SearchDbModel.class)))
                .thenThrow(new RuntimeException("DB unavailable"));

        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> consumer.consume(message)
        );

        assertEquals("DB unavailable", exception.getMessage());
    }

}