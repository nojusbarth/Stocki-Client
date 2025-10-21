package com.example.stocki_client.data.prediction;

public class PredictionDataPoint {

    private String date;
    private float pctReturn;
    private float closePrediction;
    private int confidence;


    public String getDate() {
        return date;
    }

    public float getPctReturn() {
        return pctReturn;
    }

    public float getClosePrediction() {
        return closePrediction;
    }

    public int getConfidence() {
        return confidence;
    }

}
