package com.example.stocki_client.ui.stock;

import android.graphics.Color;

import com.example.stocki_client.model.stocks.StockDataPoint;
import com.example.stocki_client.prediction.PredictionDataPoint;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class ChartBuilder {

    private List<StockDataPoint> historical;
    private List<PredictionDataPoint> prediction;

    public ChartBuilder() {
        historical = new ArrayList<>();
        prediction = new ArrayList<>();
    }

    public void setHistorical(List<StockDataPoint> historical) {
        this.historical.clear();
        this.historical = historical;
    }

    public void setPrediction(List<PredictionDataPoint> prediction) {
        this.prediction.clear();
        this.prediction = prediction;
    }

    public void buildComparisonChart(LineChart shell) {
        LineChart lineChart = shell;

        List<Entry> predictionEntry = buildPrediction();
        int predictionCount = predictionEntry.size();

        List<Entry> allHistorical = buildHistorical();
        List<Entry> lastHistorical = allHistorical.subList(allHistorical.size() - predictionCount, allHistorical.size());

        List<Entry> historicalEntry = new ArrayList<>();
        List<Entry> predictionAligned = new ArrayList<>();

        for (int i = 0; i < predictionCount; i++) {
            historicalEntry.add(new Entry(i, lastHistorical.get(i).getY())); // Y von Historical
            predictionAligned.add(new Entry(i, predictionEntry.get(i).getY())); // Y von Prediction
        }

        LineDataSet setHistoricalData = new LineDataSet(historicalEntry, "Close (Vergleich)");
        setHistoricalData.setColor(Color.BLACK);
        setHistoricalData.setLineWidth(2f);
        setHistoricalData.setDrawCircles(false);

        LineDataSet setPredictionData = new LineDataSet(predictionAligned, "Vorhersage");
        setPredictionData.setColor(Color.RED);
        setPredictionData.enableDashedLine(10f, 5f, 0f);
        setPredictionData.setLineWidth(2f);
        setPredictionData.setDrawCircles(false);

        LineData lineData = new LineData(setHistoricalData, setPredictionData);
        lineChart.setData(lineData);

        lineChart.getDescription().setText("Prediction vs. Historie");

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setTextSize(10f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


        lineChart.getAxisRight().setEnabled(false);

        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

        lineChart.invalidate();
    }



    public void buildChart(LineChart shell) {

        LineChart lineChart = shell;

        List<Entry> historicalEntry = buildHistorical();
        List<Entry> predictionEntry = buildPrediction();


        LineDataSet setHistoricalData = new LineDataSet(historicalEntry, "Close");
        setHistoricalData.setColor(Color.BLACK);
        setHistoricalData.setLineWidth(2f);
        setHistoricalData.setDrawCircles(false);


        LineDataSet setPredictionData = new LineDataSet(predictionEntry, "Vorhersage");
        setPredictionData.setColor(Color.RED);
        setPredictionData.enableDashedLine(10f, 5f, 0f);
        setPredictionData.setLineWidth(2f);
        setPredictionData.setDrawCircles(false);

        LineData lineData = new LineData(setHistoricalData, setPredictionData);
        lineChart.setData(lineData);

        lineChart.getDescription().setText("Close über Zeit");

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setTextSize(10f);
        xAxis.setValueFormatter(new DateAxisFormatter(historical, prediction));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getAxisRight().setEnabled(false);

        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

        lineChart.invalidate();
    }


    private List<Entry> buildHistorical() {
        List<Entry> historicalEntry = new ArrayList<>();
        for (int i = 0; i < historical.size(); i++) {
            historicalEntry.add(new Entry(i, (float) historical.get(i).getClose()));
        }

        return historicalEntry;
    }

    private List<Entry> buildPrediction() {
        List<Entry> predictionEntry = new ArrayList<>();
        int offset = historical.size(); // X-Werte nach den historischen Daten starten
        for (int i = 0; i < prediction.size(); i++) {
            predictionEntry.add(new Entry(offset + i, prediction.get(i).getClosePrediction()));
        }
        return predictionEntry;
    }


}
