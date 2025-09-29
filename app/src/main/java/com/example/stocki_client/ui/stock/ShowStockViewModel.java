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

public class ShowStockViewModel extends ViewModel {

    private final MutableLiveData<List<StockDataPoint>> historicalHours = new MutableLiveData<>();
    private final MutableLiveData<List<StockDataPoint>> historicalDays = new MutableLiveData<>();
    private final MutableLiveData<List<PredictionDataPoint>> predictionHours = new MutableLiveData<>();
    private final MutableLiveData<List<PredictionDataPoint>> predictionDays = new MutableLiveData<>();
    private final MutableLiveData<ModelInfo> modelInfoHours = new MutableLiveData<>();
    private final MutableLiveData<ModelInfo> modelInfoDays = new MutableLiveData<>();

    private static final int PERIOD_HISTORICAL = 14;
    private static final int PERIOD_PREDICTION = 3;



    public void loadData(String stockName) {
        loadHistorical(stockName,"1h", historicalHours);
        loadHistorical(stockName,"1d", historicalDays);
        loadPrediction(stockName,"1h", predictionHours);
        loadPrediction(stockName,"1d", predictionDays);
        loadModelInfo(stockName, "1h", modelInfoHours);
        loadModelInfo(stockName, "1d", modelInfoDays);
    }

    private void loadHistorical(String stockName, String interval, MutableLiveData<List<StockDataPoint>> targetLiveData) {
        ApiClient.getInstance().getHistorical(stockName, PERIOD_HISTORICAL, interval, new DataCallback<List<StockDataPoint>>() {
            @Override
            public void onSuccess(List<StockDataPoint> data) {
                targetLiveData.postValue(data);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void loadPrediction(String stockName, String interval, MutableLiveData<List<PredictionDataPoint>> targetLiveData) {
        ApiClient.getInstance().getPrediction(stockName, PERIOD_PREDICTION, interval, new DataCallback<List<PredictionDataPoint>>() {
            @Override
            public void onSuccess(List<PredictionDataPoint> data) {
                targetLiveData.postValue(data);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }


    private void loadModelInfo(String stockName, String interval, MutableLiveData<ModelInfo> targetLiveData) {
        ApiClient.getInstance().getModelInfo(stockName, interval, new DataCallback<ModelInfo>() {
            @Override
            public void onSuccess(ModelInfo data) {
                targetLiveData.postValue(data);
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
