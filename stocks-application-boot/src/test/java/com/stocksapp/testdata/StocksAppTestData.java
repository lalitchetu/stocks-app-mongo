package com.stocksapp.testdata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stocksapp.model.Stock;

import java.util.Arrays;
import java.util.List;

public class StocksAppTestData {
    public static final String STOCK_ID = "9937f801-7c94-4f7d-9114-55ad031a6a00";
    public static final String INVALID_STOCK_ID = "9937f801-7c94-4f7d-9114-55ad031a6a11";
    public static final String NAME = "Tesla Inc";
    public static final double CURRENT_PRICE = 900.00D;
    public static final double UPDATED_PRICE = 950.00D;
    public static final String TIME_STAMP = "2021-12-26T21:14:28+00:00";

    public static com.stocksapp.model.Stock getInternalStock() {
        com.stocksapp.model.Stock stock = new com.stocksapp.model.Stock();
        stock.setID(STOCK_ID);
        stock.setName(NAME);
        stock.setCurrentPrice(CURRENT_PRICE);
        stock.setTimeStamp(TIME_STAMP);
        return stock;
    }

    public static List<Stock> getInternalStockList() {
        return Arrays.asList(getInternalStock());
    }

    public static com.stocksapp.api.Stock getExternalStock() {
        com.stocksapp.api.Stock stock = new com.stocksapp.api.Stock();
        stock.setID(STOCK_ID);
        stock.setName(NAME);
        stock.setCurrentPrice(CURRENT_PRICE);
        stock.setTimeStamp(TIME_STAMP);
        return stock;
    }

    public static String getExternalStockAsJsonString() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(getExternalStock());
    }

    public static String getExternalStockAsJsonString(com.stocksapp.api.Stock stock) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(stock);
    }
}
