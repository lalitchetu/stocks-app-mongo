package com.stocksapp.exception;


import com.stocksapp.api.ErrorCodes;
import lombok.Data;

@Data
public class StocksAppException extends RuntimeException {

    private ErrorCodes errorCodes;
    private String message;

    public StocksAppException(ErrorCodes errorCodes, String message) {
        super(message);
        this.errorCodes = errorCodes;
        this.message = message;
    }
}
