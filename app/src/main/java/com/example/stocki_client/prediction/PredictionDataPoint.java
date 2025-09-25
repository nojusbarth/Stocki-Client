package com.example.stocki_client.prediction;

public class PredictionDataPoint {

    private String date;
    private float pctReturn;
    private float closePrediction;
    private int riskScore;


    public String getDate() {
        return date;
    }

    public float getPctReturn() {
        return pctReturn;
    }

    public float getClosePrediction() {
        return closePrediction;
    }

    public int getRiskScore() {
        return riskScore;
    }

}
