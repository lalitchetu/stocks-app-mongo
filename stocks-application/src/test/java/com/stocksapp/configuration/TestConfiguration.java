package com.stocksapp.configuration;

import com.stocksapp.dao.StocksAppRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan(basePackages = {
        "com.stocksapp.exception",
        "com.stocksapp.mapping",
        "com.stocksapp.service",
        "com.stocksapp.ws"})
public class TestConfiguration {

    @Bean
    public StocksAppRepository getStocksAppRepository() {
        return mock(StocksAppRepository.class);
    }

}
