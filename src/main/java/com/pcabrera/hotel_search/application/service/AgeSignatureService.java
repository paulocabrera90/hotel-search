package com.pcabrera.hotel_search.application.service;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AgeSignatureService {

    public String build(List<Integer> ages) {
        return ages.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}