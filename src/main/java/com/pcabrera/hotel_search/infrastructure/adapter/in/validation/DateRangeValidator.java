package com.pcabrera.hotel_search.infrastructure.adapter.in.validation;

import com.pcabrera.hotel_search.infrastructure.adapter.in.domain.SearchRequestDomain;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static java.util.Optional.ofNullable;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, SearchRequestDomain> {

    @Override
    public boolean isValid(SearchRequestDomain value, ConstraintValidatorContext context) {
        return ofNullable(value)
                .map(v -> v.checkIn() == null || v.checkOut() == null || v.checkIn().isBefore(v.checkOut()))
                .orElse(true);
    }
}