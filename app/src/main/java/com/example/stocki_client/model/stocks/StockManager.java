package com.example.stocki_client.model.stocks;

import java.util.ArrayList;
import java.util.List;

public class StockManager {

    private List<Stock> availableStocks;

    public StockManager( List<String> availableTickers) {
        availableStocks = new ArrayList<>();

        for(int i = 0; i < availableTickers.size(); ++i) {
            availableStocks.add(new Stock(availableTickers.get(i)));
        }

    }


    public List<String> getStockNames() {

        List<String> out = new ArrayList<>();

        for(int i = 0; i < availableStocks.size(); ++i) {
            out.add(availableStocks.get(i).getName());
        }

        return out;
    }


}
