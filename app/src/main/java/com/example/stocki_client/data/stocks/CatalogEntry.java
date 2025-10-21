package com.example.stocki_client.data.stocks;

public class CatalogEntry {

    double pctChange;
    double currentPrice;

    public CatalogEntry() {}


    public double getCurrentPrice() {
        return currentPrice;
    }

    public double getPctChange() {
        return pctChange;
    }
}
