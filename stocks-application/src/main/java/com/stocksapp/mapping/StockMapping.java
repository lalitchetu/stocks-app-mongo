package com.stocksapp.mapping;

import com.stocksapp.api.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class StockMapping {
    private static final Logger LOG = LoggerFactory.getLogger(StockMapping.class);

    public com.stocksapp.model.Stock mapToInternal(Stock external) {
        LOG.trace("In mapToInternal with Stock: {}", external);
        return mapToInternal(external.getID() != null ? external.getID() : UUID.randomUUID().toString(), external);
    }

    public com.stocksapp.model.Stock mapToInternal(String stockId, Stock external) {
        LOG.trace("In mapToInternal with stockId:{} and Stock: {}", stockId, external);
        com.stocksapp.model.Stock internal = new com.stocksapp.model.Stock();
        internal.setID(stockId);
        internal.setName(external.getName());
        internal.setCurrentPrice(external.getCurrentPrice());
        internal.setTimeStamp(external.getTimeStamp());
        return internal;
    }

    public Stock mapToExternal(com.stocksapp.model.Stock internal) {
        LOG.trace("In mapToExternal with Stock: {}", internal);
        Stock external = new Stock();
        external.setID(internal.getID());
        external.setName(internal.getName());
        external.setCurrentPrice(internal.getCurrentPrice());
        external.setTimeStamp(internal.getTimeStamp());
        return external;
    }

    public Page<Stock> mapToExternal(Page<com.stocksapp.model.Stock> internalList) {
        LOG.trace("In mapToExternal with StockList: {}", internalList);
        if (internalList == null) {
            return null;
        }

        List<Stock> externalList = new ArrayList<>();
        for (com.stocksapp.model.Stock internal : internalList) {
            externalList.add(mapToExternal(internal));
        }

        return new PageImpl<>(externalList, internalList.getPageable(), internalList.getSize());
    }
}
