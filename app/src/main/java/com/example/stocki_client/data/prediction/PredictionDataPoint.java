package com.example.stocki_client.data.prediction;

public class PredictionDataPoint {

    private String dateEnd;
    private float pctReturn;
    private float closePrediction;
    private float intervalBottom;
    private float intervalTop;
    private int confidence;
    private String predType;
    private String ticker; // <---- Only in Client Version, not Server


    public void setTicker(String tickerName) {
        ticker = tickerName;
    }

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

    public String getType() {
        return predType;
    }

    public float getIntervalBottom() {
        return intervalBottom;
    }

    public float getIntervalTop() {
        return intervalTop;
    }

    public String getTicker() {
        return ticker;
    }

}
