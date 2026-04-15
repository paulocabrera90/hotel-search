package com.pcabrera.hotel_search.application.service;

import com.pcabrera.hotel_search.application.service.AgeSignatureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AgeSignatureServiceTest {

    private AgeSignatureService service;

    @BeforeEach
    void setUp() {
        service = new AgeSignatureService();
    }

    @Test
    @DisplayName("Debe construir la firma de edades separada por comas")
    void shouldBuildAgeSignature() {
        List<Integer> ages = List.of(20, 35, 40);

        String result = service.build(ages);

        assertEquals("20,35,40", result);
    }

    @Test
    @DisplayName("Debe retornar una firma vacía cuando la lista está vacía")
    void shouldReturnEmptySignatureWhenListIsEmpty() {
        List<Integer> ages = List.of();

        String result = service.build(ages);

        assertEquals("", result);
    }
}