package com.pcabrera.hotel_search.infrastructure.adapter.in;

import com.pcabrera.hotel_search.application.port.in.CountSearchPortIn;
import com.pcabrera.hotel_search.application.port.in.CreateSearchPortIn;
import com.pcabrera.hotel_search.infrastructure.adapter.in.domain.CountResponseDomain;
import com.pcabrera.hotel_search.infrastructure.adapter.in.domain.SearchResponseDomain;
import com.pcabrera.hotel_search.infrastructure.config.ErrorCode;
import com.pcabrera.hotel_search.infrastructure.config.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.pcabrera.hotel_search.SearchTestMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SearchControllerTest {

    private CreateSearchPortIn createSearchPortIn;
    private CountSearchPortIn countSearchPortIn;
    private SearchController controller;

    @BeforeEach
    void setUp() {
        createSearchPortIn = mock(CreateSearchPortIn.class);
        countSearchPortIn = mock(CountSearchPortIn.class);
        controller = new SearchController(createSearchPortIn, countSearchPortIn);
    }

    @Test
    @DisplayName("Debe responder 200 cuando la request de búsqueda es válida")
    void shouldReturn200WhenSearchRequestIsValid() {
        when(createSearchPortIn.execute(any()))
                .thenReturn(validResponse());

        ResponseEntity<SearchResponseDomain> response = controller.search(validRequest());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(SEARCH_ID, response.getBody().searchId());
    }

    @Test
    @DisplayName("Debe propagar ApiException cuando la búsqueda es inválida")
    void shouldThrowApiExceptionWhenSearchIsInvalid() {
        when(createSearchPortIn.execute(any()))
                .thenThrow(new ApiException(ErrorCode.INVALID_DATE_RANGE));

        ApiException exception = assertThrows(
                ApiException.class,
                () -> controller.search(invalidDateRequest())
        );

        assertEquals(ErrorCode.INVALID_DATE_RANGE, exception.getErrorCode());
    }

    @Test
    @DisplayName("Debe propagar RuntimeException cuando ocurre un error inesperado en search")
    void shouldThrowRuntimeExceptionWhenUnexpectedErrorOccursInSearch() {
        when(createSearchPortIn.execute(any()))
                .thenThrow(new RuntimeException("Kafka unavailable"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> controller.search(validRequest())
        );

        assertEquals("Kafka unavailable", exception.getMessage());
    }

    @Test
    @DisplayName("Debe responder 200 cuando se consulta el count")
    void shouldReturn200WhenCountIsRequested() {
        when(countSearchPortIn.execute(SEARCH_ID))
                .thenReturn(validCountResponse());

        ResponseEntity<CountResponseDomain> response = controller.count(SEARCH_ID);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        CountResponseDomain body = response.getBody();
        assertEquals(SEARCH_ID, body.searchId());
        assertEquals(5L, body.count());
        assertNotNull(body.search());
        assertEquals(HOTEL_ID, body.search().hotelId());
        assertEquals(validRequest().checkIn(), body.search().checkIn());
        assertEquals(validRequest().checkOut(), body.search().checkOut());
        assertEquals(validRequest().ages(), body.search().ages());
    }

    @Test
    @DisplayName("Debe propagar ApiException cuando count recibe un searchId inválido")
    void shouldThrowApiExceptionWhenCountRequestIsInvalid() {
        when(countSearchPortIn.execute(INVALID_SEARCH_ID))
                .thenThrow(new ApiException(ErrorCode.INVALID_SEARCH_ID));

        ApiException exception = assertThrows(
                ApiException.class,
                () -> controller.count(INVALID_SEARCH_ID)
        );

        assertEquals(ErrorCode.INVALID_SEARCH_ID, exception.getErrorCode());
    }

    @Test
    @DisplayName("Debe propagar RuntimeException cuando ocurre un error inesperado en count")
    void shouldThrowRuntimeExceptionWhenUnexpectedErrorOccursInCount() {
        when(countSearchPortIn.execute(SEARCH_ID))
                .thenThrow(new RuntimeException("DB unavailable"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> controller.count(SEARCH_ID)
        );

        assertEquals("DB unavailable", exception.getMessage());
    }
}