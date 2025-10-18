package com.example.stocki_client.ui.stock;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.stocki_client.stocks.StockDataPoint;
import com.example.stocki_client.prediction.PredictionDataPoint;
import com.example.stocki_client.remote.ApiClient;
import com.example.stocki_client.remote.DataCallback;
import com.example.stocki_client.models.ModelInfo;

import java.util.List;
import java.util.Map;

public class ShowStockViewModel extends ViewModel {

    private final MutableLiveData<List<StockDataPoint>> historicalHours = new MutableLiveData<>();
    private final MutableLiveData<List<StockDataPoint>> historicalDays = new MutableLiveData<>();
    private final MutableLiveData<List<PredictionDataPoint>> predictionHours = new MutableLiveData<>();
    private final MutableLiveData<List<PredictionDataPoint>> predictionDays = new MutableLiveData<>();
    private final MutableLiveData<ModelInfo> modelInfoHours = new MutableLiveData<>();
    private final MutableLiveData<ModelInfo> modelInfoDays = new MutableLiveData<>();

    private static final int PERIOD_HISTORICAL = 14;


    public void loadData(String stockName) {
        loadHistorical(stockName,historicalHours, historicalDays);
        loadPrediction(stockName,predictionHours, predictionDays);
        loadModelInfo(stockName, modelInfoHours, modelInfoDays);
    }

    private void loadHistorical(String stockName, MutableLiveData<List<StockDataPoint>> targetLiveDataHour,
                                MutableLiveData<List<StockDataPoint>> targetLiveDataDay) {
        ApiClient.getInstance().getHistorical(stockName, PERIOD_HISTORICAL, new DataCallback<Map<String,List<StockDataPoint>>>() {
            @Override
            public void onSuccess(Map<String,List<StockDataPoint>> data) {
                targetLiveDataHour.postValue(data.get("1h"));
                targetLiveDataDay.postValue(data.get("1d"));
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void loadPrediction(String stockName, MutableLiveData<List<PredictionDataPoint>> targetLiveDataHour,
                                MutableLiveData<List<PredictionDataPoint>> targetLiveDataDay) {
        ApiClient.getInstance().getPrediction(stockName, new DataCallback<Map<String,List<PredictionDataPoint>>>() {
            @Override
            public void onSuccess(Map<String,List<PredictionDataPoint>> data) {
                targetLiveDataHour.postValue(data.get("1h"));
                targetLiveDataDay.postValue(data.get("1d"));
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }


    private void loadModelInfo(String stockName, MutableLiveData<ModelInfo> targetLiveDataHour,
                               MutableLiveData<ModelInfo> targetLiveDataDay) {
        ApiClient.getInstance().getModelInfo(stockName, new DataCallback<Map<String,ModelInfo>>() {
            @Override
            public void onSuccess(Map<String,ModelInfo> data) {
                targetLiveDataHour.postValue(data.get("1h"));
                targetLiveDataDay.postValue(data.get("1d"));
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });


    }

    public LiveData<List<StockDataPoint>> getHistorical(String interval) {
        if ("1h".equals(interval)) return historicalHours;
        else return historicalDays;
    }

    public LiveData<List<PredictionDataPoint>> getPrediction(String interval) {
        if ("1h".equals(interval)) return predictionHours;
        else return predictionDays;
    }

    public LiveData<ModelInfo> getModelInfo(String interval) {
        if ("1h".equals(interval)) return modelInfoHours;
        else return modelInfoDays;
    }
}
