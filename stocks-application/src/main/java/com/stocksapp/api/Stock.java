package com.stocksapp.api;

import lombok.Data;

@Data
public class Stock {

    private String ID;
    private String name;
    private Double currentPrice;
    private String timeStamp;

}
