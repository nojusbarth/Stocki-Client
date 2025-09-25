package com.example.stocki_client.ui.stock;

import android.graphics.Color;

import com.example.stocki_client.TimeFormatter;
import com.example.stocki_client.model.stocks.StockDataPoint;
import com.example.stocki_client.prediction.PredictionDataPoint;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PredictionChartBuilder {

    private List<StockDataPoint> historical;
    private List<PredictionDataPoint> prediction;
    private String interval;

    private TimeFormatter timeFormatter;

    public PredictionChartBuilder(String interval) {
        historical = new ArrayList<>();
        prediction = new ArrayList<>();
        this.interval = interval;
        timeFormatter = new TimeFormatter();
    }

    public void setHistorical(List<StockDataPoint> historical) {
        this.historical.clear();
        if (historical != null) {
            this.historical.addAll(historical);
        }
    }

    public void setPrediction(List<PredictionDataPoint> prediction) {
        this.prediction.clear();
        if (prediction != null) {
            this.prediction.addAll(prediction);
        }
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public void buildChart(LineChart lineChart) {
        if (lineChart == null || historical.isEmpty() || prediction.isEmpty()) return;

        List<Entry> historicalEntry = new ArrayList<>();
        List<Entry> predictionEntry = new ArrayList<>();
        final List<String> xLabels = new ArrayList<>();




        for (int i = 0; i < historical.size(); i++) {
            StockDataPoint dp = historical.get(i);
            historicalEntry.add(new Entry(i, dp.getClose()));
            String rawDate = dp.getDate();
            xLabels.add(timeFormatter.formatGraph(rawDate, interval));
        }

        int startIndex = historical.size() - 1;
        if (!historical.isEmpty() && !prediction.isEmpty()) {
            StockDataPoint lastHistorical = historical.get(historical.size() - 1);
            predictionEntry.add(new Entry(startIndex, lastHistorical.getClose()));
        }

        for (int i = 0; i < prediction.size(); i++) {
            PredictionDataPoint dp = prediction.get(i);
            predictionEntry.add(new Entry(historical.size() + i, dp.getClosePrediction()));

            String rawDate = dp.getDate();
            xLabels.add(timeFormatter.formatGraph(rawDate, interval));
        }

        LineDataSet setHistorical = new LineDataSet(historicalEntry, "Close");
        setHistorical.setColor(Color.BLACK);
        setHistorical.setLineWidth(2f);
        setHistorical.setDrawCircles(false);

        LineDataSet setPrediction = new LineDataSet(predictionEntry, "Prediction");
        setPrediction.setLineWidth(2f);
        setPrediction.setDrawCircles(false);
        setPrediction.enableDashedLine(0f, 0f, 0f);

        List<Integer> colors = new ArrayList<>();
        if (!prediction.isEmpty() && !historical.isEmpty()) {
            StockDataPoint lastHistorical = historical.get(historical.size() - 1);
            PredictionDataPoint firstPrediction = prediction.get(0);
            float deltaFirst = firstPrediction.getClosePrediction() - lastHistorical.getClose();
            colors.add(deltaFirst > 0 ? Color.GREEN : deltaFirst < 0 ? Color.RED : Color.GRAY);

            for (int i = 1; i < prediction.size(); i++) {
                PredictionDataPoint dp = prediction.get(i);
                float delta;
                delta = dp.getClosePrediction() - prediction.get(i - 1).getClosePrediction();


                if (delta > 0) {
                    colors.add(Color.GREEN);
                } else if (delta < 0) {
                    colors.add(Color.RED);
                } else {
                    colors.add(Color.GRAY);
                }
            }
        }
        setPrediction.setColors(colors);

        LineData lineData = new LineData(setHistorical, setPrediction);
        lineChart.setData(lineData);

        lineChart.getDescription().setText("Close über Zeit");

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setTextSize(10f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < xLabels.size()) {
                    return xLabels.get(index);
                }
                return "";
            }
        });

        lineChart.getAxisRight().setEnabled(false);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.invalidate();
    }

}

