package com.example.stocki_client.ui.mainpage.portfolio;

public interface OnStockSellListener {
    void onStockSoldCallback(String ticker, double sellAmount, double sellPrice);
}
