package com.stocksapp.ws;

import com.stocksapp.api.ErrorCodes;
import com.stocksapp.configuration.TestConfiguration;
import com.stocksapp.dao.StocksAppRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static com.stocksapp.testdata.StocksAppTestData.CURRENT_PRICE;
import static com.stocksapp.testdata.StocksAppTestData.INVALID_STOCK_ID;
import static com.stocksapp.testdata.StocksAppTestData.NAME;
import static com.stocksapp.testdata.StocksAppTestData.STOCK_ID;
import static com.stocksapp.testdata.StocksAppTestData.TIME_STAMP;
import static com.stocksapp.testdata.StocksAppTestData.getExternalStockAsJsonString;
import static com.stocksapp.testdata.StocksAppTestData.getInternalStock;
import static com.stocksapp.testdata.StocksAppTestData.getInternalStockList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = TestConfiguration.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(StocksAppController.class)
public class StocksAppControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private StocksAppRepository stocksAppRepository;

    @Test
    public void getStocks() throws Exception {
        //given
        when(stocksAppRepository.findAll(any(Pageable.class))).thenReturn(getInternalStockList());

        //when and then
        this.mvc.perform(get("/api/stocks?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].id").value(STOCK_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].name").value(NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].currentPrice").value(CURRENT_PRICE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].timeStamp").value(TIME_STAMP));
    }

    @Test
    public void getStockById() throws Exception {
        //given
        when(stocksAppRepository.findById(eq(STOCK_ID))).thenReturn(Optional.of(getInternalStock()));

        //when and then
        this.mvc.perform(get("/api/stocks/" + STOCK_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(STOCK_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPrice").value(CURRENT_PRICE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").value(TIME_STAMP));
    }

    @Test
    public void getStockByInvalidStockId() throws Exception {
        //given
        when(stocksAppRepository.findById(eq(INVALID_STOCK_ID))).thenReturn(Optional.empty());

        //when and then
        this.mvc.perform(get("/api/stocks/" + INVALID_STOCK_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCodes").value(ErrorCodes.INVALID_STOCK_ID.getErrorCodes()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorDescription").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorDescription").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorDescription").value(Matchers.containsString(INVALID_STOCK_ID)));
    }

    @Test
    public void createStocks() throws Exception {
        //given
        when(stocksAppRepository.save(any(com.stocksapp.model.Stock.class))).thenReturn(getInternalStock());

        //when and then
        this.mvc.perform(post("/api/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getExternalStockAsJsonString()))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(STOCK_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPrice").value(CURRENT_PRICE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").value(TIME_STAMP));
    }

    @Test
    public void updatePrice() throws Exception {
        //given
        when(stocksAppRepository.findById(eq(STOCK_ID))).thenReturn(Optional.of(getInternalStock()));
        when(stocksAppRepository.save(any(com.stocksapp.model.Stock.class))).thenReturn(getInternalStock());

        //when and then
        this.mvc.perform(patch("/api/stocks/" + STOCK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getExternalStockAsJsonString()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(STOCK_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPrice").value(CURRENT_PRICE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").value(TIME_STAMP));
    }

    @Test
    public void deleteStocks() throws Exception {
        //given
        doNothing().when(stocksAppRepository).deleteById(eq(STOCK_ID));

        //when and then
        this.mvc.perform(delete("/api/stocks/" + STOCK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getExternalStockAsJsonString()))
                .andExpect(status().isOk());
    }
}