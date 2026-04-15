package com.pcabrera.hotel_search.infrastructure.config;


public class ErrorCode {

    public static final ErrorCode INTERNAL_ERROR =
            new ErrorCode("100", "Error interno");

    public static final ErrorCode INVALID_DATE_RANGE =
            new ErrorCode("101", "CheckIn must be before checkOut");

    public static final ErrorCode INVALID_SEARCH_ID =
            new ErrorCode("102", "SearchId inválido");

    public static final ErrorCode SEARCH_NOT_FOUND =
            new ErrorCode("103", "Búsqueda no encontrada");

    public static final ErrorCode CLIENT_BAD_REQUEST =
            new ErrorCode("103", Constants.BAD_REQUEST_MESSAGE);

    private final String value;
    private final String reasonPhrase;

    public ErrorCode(String value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public String value() {
        return this.value;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    private static class Constants {
        public static final String BAD_REQUEST_MESSAGE = "Datos inválidos";
    }
}