package com.stocksapp.ws;

import com.stocksapp.api.Stock;
import com.stocksapp.service.StocksAppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StocksAppController {

    public static final String GET_STOCKS_PATH = "/api/stocks";
    public static final String CREATE_STOCK_PATH = "/api/stocks";
    public static final String GET_STOCK_BY_ID_PATH = "/api/stocks/{stockId}";
    public static final String DELETE_STOCK_BY_ID_PATH = "/api/stocks/{stockId}";
    public static final String UPDATE_STOCK_PRICE_STOCK_PATH = "/api/stocks/{stockId}";

    private static final Logger LOG = LoggerFactory.getLogger(StocksAppController.class);

    @Autowired
    private StocksAppService stocksAppService;

    @GetMapping(
            path = GET_STOCKS_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Page<Stock> getStocks(final Pageable pageable) {
        LOG.trace("In getStocks() with pageable: {}", pageable);
        return stocksAppService.getStocks(pageable);
    }

    @GetMapping(
            path = GET_STOCK_BY_ID_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Stock getStockById(@PathVariable String stockId) {
        LOG.trace("In getStocks() with stockId: {}", stockId);
        return stocksAppService.getStockById(stockId);
    }

    @PostMapping(
            path = CREATE_STOCK_PATH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Stock createStocks(@RequestBody Stock stock) {
        LOG.trace("In createStocks()");
        return stocksAppService.createStocks(stock);
    }

    @PatchMapping(
            path = UPDATE_STOCK_PRICE_STOCK_PATH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Stock updatePrice(@PathVariable String stockId, @RequestBody Stock stock) {
        LOG.trace("In updateStocks() with stockId: {} and stock: {}", stockId, stock);
        return stocksAppService.updatePrice(stockId, stock);
    }

    @DeleteMapping(DELETE_STOCK_BY_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    public void deleteStocks(@PathVariable String stockId) {
        LOG.trace("In deleteStocks() with stockId: {}", stockId);
        stocksAppService.deleteStocks(stockId);
    }
}
