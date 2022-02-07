package com.stocksapp.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@AllArgsConstructor
public enum ErrorCodes {

    INVALID_REQUEST(1001, "Invalid request", HttpStatus.BAD_REQUEST),
    INVALID_STOCK_ID(1002, "Stock with request id cannot be found", HttpStatus.BAD_REQUEST),
    INVALID_LAST_UPDATE(1002, "The value of lastUpdate is not in expected format", HttpStatus.BAD_REQUEST),
    SERVER_NOT_AVAILABLE(1010, "Server not available.", HttpStatus.SERVICE_UNAVAILABLE),
    UNKNOWN_ERROR(1011, "Unknown error.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int errorCodes;
    private final String errorDescription;
    private final HttpStatus httpStatus;
}
