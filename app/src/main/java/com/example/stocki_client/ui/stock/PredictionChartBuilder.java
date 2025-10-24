package com.example.stocki_client.ui.stock;

import android.graphics.Color;

import com.example.stocki_client.TimeFormatter;
import com.example.stocki_client.data.stocks.StockDataPoint;
import com.example.stocki_client.data.prediction.PredictionDataPoint;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class PredictionChartBuilder {

    private final List<StockDataPoint> historical;
    private final List<PredictionDataPoint> prediction;
    private String interval;

    private final TimeFormatter timeFormatter;

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
        final List<String> xLabels = new ArrayList<>();
        for (int i = 0; i < historical.size(); i++) {
            StockDataPoint dp = historical.get(i);
            historicalEntry.add(new Entry(i, dp.getClose()));
            xLabels.add(timeFormatter.formatGraph(dp.getDate(), interval));
        }

        LineDataSet setHistorical = new LineDataSet(historicalEntry, "Close");
        setHistorical.setColor(Color.BLACK);
        setHistorical.setLineWidth(2f);
        setHistorical.setDrawCircles(false);


        int startIndex = historical.size() - 1;
        StockDataPoint lastHistorical = historical.get(startIndex);

        LineData lineData;
        if ("10d".equals(interval)) {
            lineData = createIntervalLines(xLabels, startIndex, lastHistorical, setHistorical);
        } else {

            lineData = createPontLines(xLabels, startIndex, lastHistorical, setHistorical);
        }


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



    private LineData createIntervalLines(List<String> xLabels, int startIndex,
                                         StockDataPoint lastHistorical, LineDataSet setHistorical) {

        List<Entry> topLine = new ArrayList<>();
        List<Entry> bottomLine = new ArrayList<>();

        float lastClose = lastHistorical.getClose();

        topLine.add(new Entry(startIndex, lastClose));
        bottomLine.add(new Entry(startIndex, lastClose));

        List<Integer> segmentColorsTop = new ArrayList<>();
        List<Integer> segmentColorsBottom = new ArrayList<>();

        for (int i = 0; i < prediction.size(); i++) {
            PredictionDataPoint dp = prediction.get(i);
            int x = historical.size() + i;

            float topAbs = lastClose * (1 + dp.getIntervalTop() / 100f);
            float bottomAbs = lastClose * (1 + dp.getIntervalBottom() / 100f);

            topLine.add(new Entry(x, topAbs));
            bottomLine.add(new Entry(x, bottomAbs));

            int colorTop = dp.getIntervalTop() >= 0 ? Color.GREEN : Color.RED;
            int colorBottom = dp.getIntervalBottom() >= 0 ? Color.GREEN : Color.RED;

            segmentColorsTop.add(colorTop);
            segmentColorsBottom.add(colorBottom);

            xLabels.add(timeFormatter.formatGraph(dp.getDate(), interval));
        }

        LineDataSet setTop = new LineDataSet(topLine, "Interval Top");
        setTop.setLineWidth(2f);
        setTop.setDrawCircles(false);
        setTop.enableDashedLine(10f, 5f, 0f);
        setTop.setColors(segmentColorsTop);

        LineDataSet setBottom = new LineDataSet(bottomLine, "Interval Bottom");
        setBottom.setLineWidth(2f);
        setBottom.setDrawCircles(false);
        setBottom.enableDashedLine(10f, 5f, 0f);
        setBottom.setColors(segmentColorsBottom);

        return new LineData(setHistorical, setTop, setBottom);
    }


    private LineData createPontLines(List<String> xLabels, int startIndex, StockDataPoint lastHistorical,
                                     LineDataSet setHistorical) {
        List<Entry> predictionEntry = new ArrayList<>();
        predictionEntry.add(new Entry(startIndex, lastHistorical.getClose()));

        for (int i = 0; i < prediction.size(); i++) {
            PredictionDataPoint dp = prediction.get(i);
            int x = historical.size() + i;
            predictionEntry.add(new Entry(x, dp.getClosePrediction()));
            xLabels.add(timeFormatter.formatGraph(dp.getDate(), interval));
        }

        LineDataSet setPrediction = new LineDataSet(predictionEntry, "Prediction");
        setPrediction.setLineWidth(2f);
        setPrediction.setDrawCircles(false);
        setPrediction.enableDashedLine(0f, 0f, 0f);

        List<Integer> colors = new ArrayList<>();
        float deltaFirst = prediction.get(0).getPctReturn();
        colors.add(deltaFirst >= 0 ? Color.GREEN : Color.RED);

        for (int i = 1; i < prediction.size(); i++) {
            float delta = prediction.get(i).getPctReturn();
            colors.add(delta >= 0 ? Color.GREEN : Color.RED);
        }
        setPrediction.setColors(colors);

         return new LineData(setHistorical, setPrediction);
    }

}

