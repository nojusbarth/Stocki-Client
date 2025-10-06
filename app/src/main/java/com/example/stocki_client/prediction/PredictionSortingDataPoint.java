package com.example.stocki_client.prediction;

public class PredictionSortingDataPoint {

    private final String stockName;
    private final float pctReturn;
    private final int riskScore;


    public PredictionSortingDataPoint(String name, float pct, int risk) {
        stockName = name;
        pctReturn = pct;
        riskScore = risk;
    }

    public String getName() {
        return stockName;
    }

    public float getPctReturn() {
        return pctReturn;
    }


    public int getRiskScore() {
        return riskScore;
    }

}
