package com.example.stocki_client.ui.stock.model.history;

import android.graphics.Color;

import com.example.stocki_client.prediction.AccuracyDataPoint;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AccuracyChartBuilder {

    private Map<String, List<AccuracyDataPoint>> data;
    private String interval;
    private int step;

    public AccuracyChartBuilder(String interval, int step) {
        this.interval = interval;
        this.step = step-1;
        this.data = new HashMap<>();
    }

    public void setData(Map<String, List<AccuracyDataPoint>> newData) {
        if (newData == null) return;
        data.clear();
        data.putAll(newData);
    }

    public void buildChart(LineChart shell) {
        if (shell == null || data.isEmpty()) return;

        List<Entry> actualEntries = new ArrayList<>();
        List<Entry> predictionEntries = new ArrayList<>();
        final List<String> xLabels = new ArrayList<>();

        SimpleDateFormat inputFormat;
        SimpleDateFormat outputFormat;
        if ("1h".equals(interval)) {
            inputFormat = new SimpleDateFormat("yyyy-MM-dd-HH", Locale.getDefault());
            outputFormat = new SimpleDateFormat("dd.MM HH'h'", Locale.getDefault());
        } else {
            inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            outputFormat = new SimpleDateFormat("dd.MM", Locale.getDefault());
        }

        List<String> sortedKeys = new ArrayList<>(data.keySet());
        Collections.sort(sortedKeys, (d1, d2) -> {
            try {
                return inputFormat.parse(d1).compareTo(inputFormat.parse(d2));
            } catch (ParseException e) {
                return d1.compareTo(d2);
            }
        });

        int index = 0;
        for (String key : sortedKeys) {
            List<AccuracyDataPoint> points = data.get(key);
            if (points == null || points.size() <= step) continue;

            AccuracyDataPoint dp = points.get(step);
            if (dp == null) continue;

            actualEntries.add(new Entry(index, dp.getActualClose()));
            predictionEntries.add(new Entry(index, dp.getClosePrediction()));

            try {
                Date parsed = inputFormat.parse(key);
                xLabels.add(outputFormat.format(parsed));
            } catch (ParseException e) {
                xLabels.add(key);
            }

            index++;
        }

        LineDataSet actualSet = new LineDataSet(actualEntries, "Actual Close");
        actualSet.setColor(Color.BLACK);
        actualSet.setLineWidth(2f);
        actualSet.setDrawCircles(false);

        LineDataSet predictionSet = new LineDataSet(predictionEntries, "Prediction");
        predictionSet.setColor(Color.RED);
        predictionSet.enableDashedLine(10f, 5f, 0f);
        predictionSet.setLineWidth(2f);
        predictionSet.setDrawCircles(false);

        LineData lineData = new LineData(actualSet, predictionSet);
        shell.setData(lineData);

        shell.getDescription().setText("Prediction vs Actual");

        XAxis xAxis = shell.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int i = (int) value;
                if (i >= 0 && i < xLabels.size()) {
                    return xLabels.get(i);
                }
                return "";
            }
        });

        shell.getAxisRight().setEnabled(false);
        shell.setDragEnabled(true);
        shell.setScaleEnabled(true);
        shell.invalidate();
    }

}

