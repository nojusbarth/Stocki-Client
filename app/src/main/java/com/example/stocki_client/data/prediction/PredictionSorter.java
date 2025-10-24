package com.example.stocki_client.data.prediction;

//SORTS PREDICTIONS INTO TWO SETS: return < 0: ASC LOSERS, return >= 0: DESC WINNERS

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PredictionSorter {

    private List<PredictionDataPoint> currentData;

    public void setData(Map<String, PredictionDataPoint> newData) {
        currentData = new ArrayList<>();

        for (Map.Entry<String, PredictionDataPoint> entry : newData.entrySet()) {
            PredictionDataPoint p = entry.getValue();
            p.setTicker(entry.getKey());
            currentData.add(p);
        }
    }

    public List<PredictionDataPoint> getWinners() {
        List<PredictionDataPoint> winners = currentData.stream()
                .filter(p -> getSortValue(p) >= 0)
                .sorted(Comparator.comparingDouble(this::getSortValue).reversed())
                .collect(Collectors.toList());

        return winners;
    }

    public List<PredictionDataPoint> getLosers() {
        List<PredictionDataPoint> losers = currentData.stream()
                .filter(p -> getSortValue(p) < 0)
                .sorted(Comparator.comparingDouble(this::getSortValue))
                .collect(Collectors.toList());

        return losers;
    }

    /**
     * Liefert den Wert, nach dem sortiert werden soll:
     * - Bei "point" = pctReturn
     * - Bei "interval" = Mittelwert aus (intervalTop + intervalBottom) / 2
     */
    private double getSortValue(PredictionDataPoint p) {
        if ("interval".equalsIgnoreCase(p.getType())) {
            return (p.getIntervalTop() + p.getIntervalBottom()) / 2.0;
        } else {
            return p.getPctReturn();
        }
    }
}
