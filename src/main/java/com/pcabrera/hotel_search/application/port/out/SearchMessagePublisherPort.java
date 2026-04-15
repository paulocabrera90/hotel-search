package com.pcabrera.hotel_search.application.port.out;

import com.pcabrera.hotel_search.domain.model.SearchMessage;

public interface SearchMessagePublisherPort {
    void send(SearchMessage message);
}