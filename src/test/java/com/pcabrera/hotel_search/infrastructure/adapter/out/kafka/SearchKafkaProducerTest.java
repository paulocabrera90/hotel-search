package com.pcabrera.hotel_search.infrastructure.adapter.out.kafka;

import com.pcabrera.hotel_search.KafkaTestMock;
import com.pcabrera.hotel_search.domain.model.SearchMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import static com.pcabrera.hotel_search.KafkaTestMock.TOPIC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SearchKafkaProducerTest {

    private KafkaTemplate<String, SearchMessage> kafkaTemplate;
    private SearchKafkaProducer producer;


    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        producer = new SearchKafkaProducer(kafkaTemplate, TOPIC);
    }

    @Test
    @DisplayName("Debe enviar el mensaje a Kafka con topic, key y payload correctos")
    void shouldSendMessageToKafkaWithCorrectTopicKeyAndPayload() {
        SearchMessage message = KafkaTestMock.validMessage();

        producer.send(message);

        verify(kafkaTemplate, times(1))
                .send(TOPIC, message.searchId(), message);
    }

    @Test
    @DisplayName("Debe propagar excepción cuando Kafka falla")
    void shouldThrowExceptionWhenKafkaFails() {
        SearchMessage message = KafkaTestMock.validMessage();

        when(kafkaTemplate.send(anyString(), anyString(), any()))
                .thenThrow(new RuntimeException("Kafka unavailable"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> producer.send(message)
        );

        assertEquals("Kafka unavailable", exception.getMessage());
    }
}