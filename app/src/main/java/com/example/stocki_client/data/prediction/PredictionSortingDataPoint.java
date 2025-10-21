package com.example.stocki_client.data.prediction;

public class PredictionSortingDataPoint {

    private final String stockName;
    private final float pctReturn;
    private final int confidence;


    public PredictionSortingDataPoint(String name, float pct, int risk) {
        stockName = name;
        pctReturn = pct;
        confidence = risk;
    }

    public String getName() {
        return stockName;
    }

    public float getPctReturn() {
        return pctReturn;
    }


    public int getConfidence() {
        return confidence;
    }

}
