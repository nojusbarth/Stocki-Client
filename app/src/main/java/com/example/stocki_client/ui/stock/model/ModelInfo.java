package com.example.stocki_client.ui.stock.model;

import java.util.List;
import java.util.Map;

public class ModelInfo {
    private String latestUpdate;
    private String trainUntil;
    private Map<String, Double> metrics;
    private List<String> features;
    private Map<String, Object> hyperParameters;
    private int numSamples;
    private float trainTestSplit;

    public ModelInfo() {}

    public String getLatestUpdate() { return latestUpdate; }
    public void setLatestUpdate(String latestUpdate) { this.latestUpdate = latestUpdate; }

    public String getTrainUntil() { return trainUntil; }
    public void setTrainUntil(String trainUntil) { this.trainUntil = trainUntil; }

    public Map<String, Double> getMetrics() { return metrics; }
    public void setMetrics(Map<String, Double> metrics) { this.metrics = metrics; }

    public List<String> getFeatures() { return features; }
    public void setFeatures(List<String> features) { this.features = features; }

    public Map<String, Object> getHyperParameters() { return hyperParameters; }
    public void setHyperParameters(Map<String, Object> hyperParameters) { this.hyperParameters = hyperParameters; }

    public int getNumSamples() { return numSamples; }
    public void setNumSamples(int numSamples) { this.numSamples = numSamples; }

    public float getTrainTestSplit() { return trainTestSplit; }
    public void setTrainTestSplit(float trainTestSplit) { this.trainTestSplit = trainTestSplit; }
}

