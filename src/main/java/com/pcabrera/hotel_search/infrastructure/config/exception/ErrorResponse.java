package com.pcabrera.hotel_search.infrastructure.config.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        String code,
        String message,
        String path,
        LocalDateTime timestamp
) {}