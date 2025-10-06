package com.example.stocki_client.prediction;

import com.example.stocki_client.TimeFormatter;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

        List<Map.Entry<String, List<AccuracyDataPoint>>> sortedEntries = new ArrayList<>(map.entrySet());
        sortedEntries.sort(Map.Entry.comparingByKey());

        for (Map.Entry<String, List<AccuracyDataPoint>> entry : sortedEntries) {
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

        return result;
    }


}
