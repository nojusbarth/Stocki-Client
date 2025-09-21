package com.example.stocki_client.prediction;

//SORTS PREDICTIONS INTO TWO SETS: return < 0: ASC LOSERS, return >= 0: DESC WINNERS

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PredictionSorter {


    private List<PredictionSortingDataPoint> currentData;


    public void setData(Map<String, PredictionDataPoint> newData) {
        currentData = new ArrayList<>();

        for (Map.Entry<String, PredictionDataPoint> entry : newData.entrySet()) {

            float pct = entry.getValue().getPctReturn();
            int risk = entry.getValue().getRiskScore();

            currentData.add(new PredictionSortingDataPoint(entry.getKey(), pct, risk ));
        }
    }


    public List<PredictionSortingDataPoint> getWinners() {
        List<PredictionSortingDataPoint> winners = new ArrayList<>();

        for (PredictionSortingDataPoint point : currentData) {
            if (point.getPctReturn() >= 0) {
                winners.add(point);
            }
        }

        winners.sort(Comparator.comparing(PredictionSortingDataPoint::getPctReturn).reversed());

        return winners;
    }

    public List<PredictionSortingDataPoint> getLosers() {
        List<PredictionSortingDataPoint> losers = new ArrayList<>();

        for (PredictionSortingDataPoint point : currentData) {
            if (point.getPctReturn() < 0) {
                losers.add(point);
            }
        }

        losers.sort(Comparator.comparing(PredictionSortingDataPoint::getPctReturn));

        return losers;
    }
}
