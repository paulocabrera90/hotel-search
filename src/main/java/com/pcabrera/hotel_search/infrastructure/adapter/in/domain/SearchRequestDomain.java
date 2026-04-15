package com.pcabrera.hotel_search.infrastructure.adapter.in.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pcabrera.hotel_search.infrastructure.adapter.in.validation.ValidDateRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@ValidDateRange
public record SearchRequestDomain(
        @NotBlank String hotelId,

        @NotNull
        LocalDate checkIn,

        @NotNull
        LocalDate checkOut,

        @NotEmpty
        List<Integer> ages
) {
    public SearchRequestDomain {
        ages = List.copyOf(ages);
    }
}