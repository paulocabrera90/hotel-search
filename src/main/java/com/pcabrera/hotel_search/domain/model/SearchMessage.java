package com.pcabrera.hotel_search.domain.model;

import java.time.LocalDate;
import java.util.List;

public record SearchMessage(
        String searchId,
        String hotelId,
        LocalDate checkIn,
        LocalDate checkOut,
        List<Integer> ages
) {
    public SearchMessage {
        ages = List.copyOf(ages);
    }
}