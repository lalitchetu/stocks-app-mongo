package com.stocksapp.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("stockitems")
public class Stock {
    @Id
    private String ID;
    private String name;
    private Double currentPrice;
    private String timeStamp;

}
