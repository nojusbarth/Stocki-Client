package com.example.stocki_client.models;

import java.util.Map;

public class ModelInfo {
    private String latestUpdate;
    private Map<String, Double> metrics;


    public ModelInfo() {}

    public String getLatestUpdate() {
        return latestUpdate;
    }

    public Map<String, Double> getMetrics() {
        return metrics;
    }
}

