package com.stocksapp.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
@ComponentScan(basePackages = {
        "com.stocksapp.exception",
        "com.stocksapp.mapping",
        "com.stocksapp.service",
        "com.stocksapp.ws"})
public class StocksApplicationConfiguration {

}
