package com.example.stocki_client.prediction;

public class AccuracyDataPoint {
    private float closePrediction;
    private int riskPrediction;
    private float actualClose;
    private float pctReturnPrediction;




    public float getActualClose() {
        return actualClose;
    }

    public float getClosePrediction() {
        return closePrediction;
    }

    public int getRiskPrediction() {
        return riskPrediction;
    }

    public float getPctReturnPrediction() {
        return pctReturnPrediction;
    }

}
