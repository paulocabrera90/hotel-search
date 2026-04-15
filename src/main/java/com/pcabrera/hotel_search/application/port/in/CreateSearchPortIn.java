package com.pcabrera.hotel_search.application.port.in;

import com.pcabrera.hotel_search.infrastructure.adapter.in.domain.SearchRequestDomain;
import com.pcabrera.hotel_search.infrastructure.adapter.in.domain.SearchResponseDomain;

public interface CreateSearchPortIn {
    SearchResponseDomain execute(SearchRequestDomain request);
}
