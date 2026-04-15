package com.pcabrera.hotel_search.application.port.in;

import com.pcabrera.hotel_search.infrastructure.adapter.in.domain.CountResponseDomain;

public interface CountSearchPortIn {
    CountResponseDomain execute(String searchId);
}