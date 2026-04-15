package com.pcabrera.hotel_search.domain.model;

import java.time.LocalDate;
import java.util.List;

public record SearchResultDomain(
        String hotelId,
        LocalDate checkIn,
        LocalDate checkOut,
        List<Integer> ages
) {
}
