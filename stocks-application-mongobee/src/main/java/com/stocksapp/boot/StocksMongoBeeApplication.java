package com.stocksapp.boot;

import com.stocksapp.mongobee.MongoBeeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@Import({MongoBeeConfig.class})
public class StocksMongoBeeApplication {
    private static final Logger LOG = LoggerFactory.getLogger(StocksMongoBeeApplication.class);

    public static void main(String[] args) {
        LOG.info("STARTING THE STOCKS APPLICATION");
        SpringApplication.run(StocksMongoBeeApplication.class, args);
    }
}
