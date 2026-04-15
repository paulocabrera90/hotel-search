package com.pcabrera.hotel_search.application.usecase;

import com.pcabrera.hotel_search.application.port.in.CountSearchPortIn;
import com.pcabrera.hotel_search.application.service.CountSearchService;
import com.pcabrera.hotel_search.infrastructure.adapter.in.domain.CountResponseDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CountSearchUseCase implements CountSearchPortIn {

    private final CountSearchService countSearchService;
    @Override
    public CountResponseDomain execute(String searchId) {
        return countSearchService.countBySearchId(searchId);
    }
}