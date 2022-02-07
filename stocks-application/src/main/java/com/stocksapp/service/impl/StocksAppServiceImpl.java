package com.stocksapp.service.impl;

import com.stocksapp.api.ErrorCodes;
import com.stocksapp.api.Stock;
import com.stocksapp.service.StocksAppService;
import com.stocksapp.dao.StocksAppRepository;
import com.stocksapp.exception.StocksAppException;
import com.stocksapp.mapping.StockMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StocksAppServiceImpl implements StocksAppService {
    private static final Logger LOG = LoggerFactory.getLogger(StocksAppService.class);

    @Autowired
    private StockMapping mapping;

    @Autowired(required = false)
    private StocksAppRepository stocksAppRepository;

    @Override
    public Page<Stock> getStocks(final Pageable pageable) {
        LOG.trace("In getStocks() with pageable: {}", pageable);
        return mapping.mapToExternal(stocksAppRepository.findAll(pageable));
    }

    @Override
    public Stock getStockById(final String stockId) {
        LOG.trace("In getStockById() with stockId: {}", stockId);
        return mapping.mapToExternal(stocksAppRepository.findById(stockId).orElseThrow(() -> new StocksAppException(ErrorCodes.INVALID_STOCK_ID, "Stock with Id: " + stockId + " not found")));
    }

    @Override
    public Stock createStocks(final Stock stock) {
        LOG.trace("In createStocks() with stock: {}", stock);
        return mapping.mapToExternal(stocksAppRepository.save(mapping.mapToInternal(stock)));
    }

    @Override
    public Stock updatePrice(final String stockId, final Stock stock) {
        LOG.trace("In updateStocks() with stockId: {} and stock: {}", stockId, stock);
        com.stocksapp.model.Stock internalStock = stocksAppRepository.findById(stockId).orElseThrow(() -> new StocksAppException(ErrorCodes.INVALID_STOCK_ID, "Stock with Id: " + stockId + " not found"));
        internalStock.setCurrentPrice(stock.getCurrentPrice());

        return mapping.mapToExternal(stocksAppRepository.save(internalStock));
    }

    @Override
    public void deleteStocks(final String stockId) {
        LOG.trace("In deleteStocks() with stockId: {}", stockId);
        try {
            stocksAppRepository.deleteById(stockId);
        } catch (EmptyResultDataAccessException ex) {
            LOG.error("Stock with id: {} not found", stockId, ex);
            throw new StocksAppException(ErrorCodes.INVALID_STOCK_ID, "Stock with Id: " + stockId + " not found");
        }
    }
}
