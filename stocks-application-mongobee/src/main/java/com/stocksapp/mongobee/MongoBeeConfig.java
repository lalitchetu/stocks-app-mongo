package com.stocksapp.mongobee;

import com.github.mongobee.Mongobee;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoBeeConfig {

    @Value("${spring.data.mongodb.host}")
    private String mongodbHost;

    @Value("${spring.data.mongodb.port}")
    private String mongodbPort;

    @Value("${spring.data.mongodb.database}")
    private String mongodbDatabase;

    @Bean
    public Mongobee mongobee() {
        Mongobee runner = new Mongobee(createMongoURI());
        runner.setDbName(this.mongodbDatabase);
        runner.setChangeLogsScanPackage("com.stocksapp.changeLogs");
        return runner;
    }

    private String createMongoURI() {
        //"mongodb://localhost:27017"
        StringBuilder sb = new StringBuilder();
        sb.append("mongodb://");
        sb.append(this.mongodbHost);
        sb.append(":");
        sb.append(this.mongodbPort);
        return sb.toString();
    }
}
