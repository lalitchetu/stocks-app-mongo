package com.stocksapp.api;

import lombok.Data;

@Data
public class StocksAppError {

    private int errorCodes;
    private String errorDescription;

}
