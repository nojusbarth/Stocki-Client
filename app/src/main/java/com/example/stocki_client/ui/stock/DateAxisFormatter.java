package com.example.stocki_client.ui.stock;


import com.example.stocki_client.model.stocks.StockDataPoint;
import com.example.stocki_client.prediction.PredictionDataPoint;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.List;

public class DateAxisFormatter extends ValueFormatter {
    private final List<StockDataPoint> stockPoints;
    private final List<PredictionDataPoint> predictionPoints;

    public DateAxisFormatter(List<StockDataPoint> stockPoints, List<PredictionDataPoint> predictionPoints) {
        this.stockPoints = stockPoints;
        this.predictionPoints = predictionPoints;

    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        int index = (int) value;

        if (index < stockPoints.size()) {
            // Historische Daten
            String fullDate = stockPoints.get(index).getDate();
            String[] parts = fullDate.split("-");
            return parts[2] + "." + parts[1]; // Tag.Monat
        }
        else if (index - stockPoints.size() < predictionPoints.size()) {
            // Vorhersagedaten
            int predIndex = index - stockPoints.size();
            String fullDate = predictionPoints.get(predIndex).getDate();
            String[] parts = fullDate.split("-");
            return parts[2] + "." + parts[1];
        }
        else {
            return "";
        }
    }
}