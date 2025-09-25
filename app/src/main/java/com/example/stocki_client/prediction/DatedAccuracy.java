package com.example.stocki_client.prediction;

import com.example.stocki_client.TimeFormatter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class DatedAccuracy {

    private final String date;
    private final AccuracyDataPoint data;

    public DatedAccuracy(String date, AccuracyDataPoint data) {
        this.date = date;
        this.data = data;
    }

    public String getDate() {
        return date;
    }

    public AccuracyDataPoint getData() {
        return data;
    }

    public static List<DatedAccuracy> extractStepData(Map<String, List<AccuracyDataPoint>> map, int step, String interval) {
        List<DatedAccuracy> result = new ArrayList<>();
        TimeFormatter timeFormatter = new TimeFormatter();
        if (map == null) return result;

        for (Map.Entry<String, List<AccuracyDataPoint>> entry : map.entrySet()) {
            String dateRaw = entry.getKey();

            String date = timeFormatter.formatCV(dateRaw, interval);

            List<AccuracyDataPoint> points = entry.getValue();

            if (points != null && step >= 0 && step < points.size()) {
                AccuracyDataPoint dp = points.get(step);
                if (dp != null) {
                    result.add(new DatedAccuracy(date, dp));
                }
            }
        }

        result.sort(Comparator.comparing(DatedAccuracy::getDate));

        return result;
    }

}
