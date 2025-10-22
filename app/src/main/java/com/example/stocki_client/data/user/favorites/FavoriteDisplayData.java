package com.example.stocki_client.data.user.favorites;

import com.example.stocki_client.data.prediction.PredictionDataPoint;

public class FavoriteDisplayData {
    private String ticker;
    private PredictionDataPoint hourPrediction;
    private PredictionDataPoint dayPrediction;

    public FavoriteDisplayData(String ticker,
                               PredictionDataPoint hourPrediction,
                               PredictionDataPoint dayPrediction) {
        this.ticker = ticker;
        this.hourPrediction = hourPrediction;
        this.dayPrediction = dayPrediction;
    }


    public String getTicker() {
        return ticker;
    }

    public float getPctReturnDay() {
        return dayPrediction.getPctReturn();
    }

    public float getPctReturnHour() {
        return hourPrediction.getPctReturn();
    }
}
