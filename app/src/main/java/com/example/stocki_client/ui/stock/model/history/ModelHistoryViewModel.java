package com.example.stocki_client.ui.stock.model.history;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.stocki_client.prediction.AccuracyDataPoint;
import com.example.stocki_client.remote.ApiClient;
import com.example.stocki_client.remote.DataCallback;

import java.util.List;
import java.util.Map;

public class ModelHistoryViewModel extends ViewModel {


    private final MutableLiveData<Map<String, List<AccuracyDataPoint>>> accuracy = new MutableLiveData<>();

    private static final int PERIOD_PREDICTION_HISTORICAL = 5;

    private String interval;

    public void initData(String stockName,String interval) {
        loadPrediction(stockName,interval, accuracy);
        this.interval = interval;
    }

    public String getInterval() {
        return interval;
    }

    private void loadPrediction(String stockName, String interval, MutableLiveData<Map<String, List<AccuracyDataPoint>>> targetLiveData) {
        ApiClient.getInstance().getAccuracy(stockName, PERIOD_PREDICTION_HISTORICAL, interval,
                new DataCallback<Map<String, List<AccuracyDataPoint>>>() {
            @Override
            public void onSuccess(Map<String, List<AccuracyDataPoint>> data) {
                targetLiveData.postValue(data);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }


    public LiveData<Map<String, List<AccuracyDataPoint>>> getAccuracy() {
       return accuracy;
    }

}
