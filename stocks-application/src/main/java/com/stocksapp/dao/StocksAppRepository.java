package com.stocksapp.dao;

import com.stocksapp.model.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StocksAppRepository extends MongoRepository<Stock, String> {
}
