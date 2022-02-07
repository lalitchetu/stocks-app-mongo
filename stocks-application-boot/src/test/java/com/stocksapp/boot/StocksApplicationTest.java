package com.stocksapp.boot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stocksapp.api.ErrorCodes;
import com.stocksapp.model.Stock;
import com.stocksapp.testdata.StocksAppTestData;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.InputStream;
import java.util.List;

import static com.stocksapp.testdata.StocksAppTestData.CURRENT_PRICE;
import static com.stocksapp.testdata.StocksAppTestData.NAME;
import static com.stocksapp.testdata.StocksAppTestData.STOCK_ID;
import static com.stocksapp.testdata.StocksAppTestData.TIME_STAMP;
import static com.stocksapp.testdata.StocksAppTestData.getExternalStockAsJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class StocksApplicationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void importJSON(String collection, String jsonFileName) {

        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(jsonFileName)) {
            ObjectMapper mapper = new ObjectMapper();
            List<com.stocksapp.model.Stock> stocks = mapper.readValue(inputStream, new TypeReference<List<Stock>>() {
            });
            for (com.stocksapp.model.Stock stock : stocks) {
                mongoTemplate.save(stock, collection);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setup() {
        importJSON("stockitems", "test-data.json");
    }

    @Test
    @Disabled
    public void getStocks() throws Exception {
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
        //when and then
        this.mvc.perform(MockMvcRequestBuilders.get("/api/stocks/" + StocksAppTestData.STOCK_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(StocksAppTestData.STOCK_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(StocksAppTestData.NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPrice").value(StocksAppTestData.CURRENT_PRICE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").value(StocksAppTestData.TIME_STAMP));
    }

    @Test
    public void getStockByInvalidId() throws Exception {
        //when and then
        this.mvc.perform(MockMvcRequestBuilders.get("/api/stocks/" + StocksAppTestData.INVALID_STOCK_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCodes").value(ErrorCodes.INVALID_STOCK_ID.getErrorCodes()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorDescription").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorDescription").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorDescription").value(Matchers.containsString(StocksAppTestData.INVALID_STOCK_ID)));
    }

    @Test
    public void createStocks() throws Exception {
        //given
        //when(stocksAppRepository.save(any(com.stocksapp.model.Stock.class))).thenReturn(getInternalStock());

        //when and then
        this.mvc.perform(post("/api/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(StocksAppTestData.getExternalStockAsJsonString()))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(StocksAppTestData.STOCK_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(StocksAppTestData.NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPrice").value(StocksAppTestData.CURRENT_PRICE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").value(StocksAppTestData.TIME_STAMP));
    }

    @Test
    public void updatePrice() throws Exception {
        //given
        com.stocksapp.api.Stock stock = StocksAppTestData.getExternalStock();
        stock.setCurrentPrice(StocksAppTestData.UPDATED_PRICE);

        //when and then
        this.mvc.perform(MockMvcRequestBuilders.patch("/api/stocks/" + StocksAppTestData.STOCK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(StocksAppTestData.getExternalStockAsJsonString(stock)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(StocksAppTestData.STOCK_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(StocksAppTestData.NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPrice").value(StocksAppTestData.UPDATED_PRICE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").value(StocksAppTestData.TIME_STAMP));

        //and when and then
        this.mvc.perform(MockMvcRequestBuilders.get("/api/stocks/" + StocksAppTestData.STOCK_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(StocksAppTestData.STOCK_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(StocksAppTestData.NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPrice").value(StocksAppTestData.UPDATED_PRICE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").value(StocksAppTestData.TIME_STAMP));
    }

    @Test
    public void updatePriceWithInvalidId() throws Exception {
        //given
        com.stocksapp.api.Stock stock = StocksAppTestData.getExternalStock();
        stock.setCurrentPrice(StocksAppTestData.UPDATED_PRICE);

        //when and then
        this.mvc.perform(MockMvcRequestBuilders.patch("/api/stocks/" + StocksAppTestData.INVALID_STOCK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(StocksAppTestData.getExternalStockAsJsonString(stock)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCodes").value(ErrorCodes.INVALID_STOCK_ID.getErrorCodes()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorDescription").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorDescription").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorDescription").value(Matchers.containsString(StocksAppTestData.INVALID_STOCK_ID)));

        //and when and then
        this.mvc.perform(MockMvcRequestBuilders.get("/api/stocks/" + StocksAppTestData.STOCK_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(StocksAppTestData.STOCK_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(StocksAppTestData.NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPrice").value(StocksAppTestData.CURRENT_PRICE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").value(StocksAppTestData.TIME_STAMP));
    }

    @Test
    public void deleteStocks() throws Exception {
        //given

        //when and then
        this.mvc.perform(MockMvcRequestBuilders.delete("/api/stocks/" + StocksAppTestData.STOCK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(StocksAppTestData.getExternalStockAsJsonString()))
                .andExpect(status().isOk());

        //and when and then
        this.mvc.perform(MockMvcRequestBuilders.get("/api/stocks/" + StocksAppTestData.STOCK_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCodes").value(ErrorCodes.INVALID_STOCK_ID.getErrorCodes()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorDescription").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorDescription").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorDescription").value(Matchers.containsString(StocksAppTestData.STOCK_ID)));
    }
}