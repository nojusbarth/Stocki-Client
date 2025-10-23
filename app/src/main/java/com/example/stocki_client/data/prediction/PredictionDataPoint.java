package com.example.stocki_client.data.prediction;

public class PredictionDataPoint {

    private String dateEnd;
    private float pctReturn;
    private float closePrediction;
    private float intervalBottom;
    private float intervalTop;
    private int confidence;
    private String predType;


    public String getDate() {
        return dateEnd;
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
