package com.example.stocki_client.data.prediction;

public class AccuracyDataPoint {
    private float closePrediction;
    private int confidence;
    private float actualClose;
    private float pctReturnPrediction;




    public float getActualClose() {
        return actualClose;
    }

    public float getClosePrediction() {
        return closePrediction;
    }

    public int getConfidence() {
        return confidence;
    }

    public float getPctReturnPrediction() {
        return pctReturnPrediction;
    }

}
