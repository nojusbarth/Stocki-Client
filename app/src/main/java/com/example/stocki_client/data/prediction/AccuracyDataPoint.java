package com.example.stocki_client.data.prediction;

public class AccuracyDataPoint {
    private float closePrediction;
    private int confidence;
    private float actualClose;
    private float pctReturnPrediction;

    private String predType;
    private float intervalBottom;
    private float intervalTop;


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

    public float getIntervalBottom() {
        return intervalBottom;
    }
    public float getIntervalTop() {
        return intervalTop;
    }

    public String getPredType() {
        return predType;
    }

}
