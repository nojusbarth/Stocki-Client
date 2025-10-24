package com.example.stocki_client.ui.stock;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.stocki_client.data.stocks.StockDataPoint;
import com.example.stocki_client.data.prediction.PredictionDataPoint;
import com.example.stocki_client.remote.ApiClient;
import com.example.stocki_client.remote.DataCallback;
import com.example.stocki_client.data.models.ModelInfo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowStockViewModel extends ViewModel {

    private static final int PERIOD_HISTORICAL = 14;

    private final Map<String, MutableLiveData<List<StockDataPoint>>> historicalMap = new HashMap<>();
    private final Map<String, MutableLiveData<List<PredictionDataPoint>>> predictionMap = new HashMap<>();
    private final Map<String, MutableLiveData<ModelInfo>> modelInfoMap = new HashMap<>();

    public ShowStockViewModel() {
        for (String interval : Arrays.asList("1h", "1d", "10d")) {
            historicalMap.put(interval, new MutableLiveData<>());
            predictionMap.put(interval, new MutableLiveData<>());
            modelInfoMap.put(interval, new MutableLiveData<>());
        }
    }

    public void loadData(String stockName) {
        loadHistorical(stockName);
        loadPrediction(stockName);
        loadModelInfo(stockName);
    }


    private void loadHistorical(String stockName) {
        ApiClient.getInstance().getHistorical(stockName, PERIOD_HISTORICAL, new DataCallback<Map<String, List<StockDataPoint>>>() {
            @Override
            public void onSuccess(Map<String, List<StockDataPoint>> data) {
                data.forEach((interval, list) -> {
                    MutableLiveData<List<StockDataPoint>> liveData = historicalMap.get(interval);
                    if (liveData != null) liveData.postValue(list);
                });
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }


    private void loadPrediction(String stockName) {
        ApiClient.getInstance().getPrediction(stockName, new DataCallback<Map<String, List<PredictionDataPoint>>>() {
            @Override
            public void onSuccess(Map<String, List<PredictionDataPoint>> data) {
                data.forEach((interval, list) -> {
                    MutableLiveData<List<PredictionDataPoint>> liveData = predictionMap.get(interval);
                    if (liveData != null) liveData.postValue(list);
                });
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void loadModelInfo(String stockName) {
        ApiClient.getInstance().getModelInfo(stockName, new DataCallback<Map<String, ModelInfo>>() {
            @Override
            public void onSuccess(Map<String, ModelInfo> data) {
                data.forEach((interval, info) -> {
                    MutableLiveData<ModelInfo> liveData = modelInfoMap.get(interval);
                    if (liveData != null) liveData.postValue(info);
                });
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }


    public LiveData<List<StockDataPoint>> getHistorical(String interval) {
        return historicalMap.getOrDefault(interval, new MutableLiveData<>());
    }

    public LiveData<List<PredictionDataPoint>> getPrediction(String interval) {
        return predictionMap.getOrDefault(interval, new MutableLiveData<>());
    }

    public LiveData<ModelInfo> getModelInfo(String interval) {
        return modelInfoMap.getOrDefault(interval, new MutableLiveData<>());
    }
}

