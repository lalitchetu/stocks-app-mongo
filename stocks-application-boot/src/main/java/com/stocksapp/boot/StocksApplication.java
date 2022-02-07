package com.stocksapp.boot;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "com.stocksapp.dao")
@SpringBootApplication
@Import({StocksApplicationConfiguration.class})
public class StocksApplication {
    private static final Logger LOG = LoggerFactory.getLogger(StocksApplication.class);

    public static void main(String[] args) {
        LOG.info("STARTING THE STOCKS APPLICATION");
        SpringApplication.run(StocksApplication.class, args);
    }
}
