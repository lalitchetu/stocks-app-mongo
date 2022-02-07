package com.stocksapp.service;

import com.stocksapp.api.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface StocksAppService {

    Page<Stock> getStocks(final Pageable pageable);

    Stock getStockById(final String stockId);

    Stock createStocks(final Stock stock);

    Stock updatePrice(final String stockId, final Stock stock);

    void deleteStocks(final String stockId);
}
