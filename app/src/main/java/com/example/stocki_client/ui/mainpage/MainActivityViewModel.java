package com.example.stocki_client.ui.mainpage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.stocki_client.prediction.PredictionDataPoint;
import com.example.stocki_client.remote.ApiClient;
import com.example.stocki_client.remote.DataCallback;

import java.util.Map;

public class MainActivityViewModel extends ViewModel {

    private final MutableLiveData<Map<String, PredictionDataPoint>> predictionsDay = new MutableLiveData<>();
    private final MutableLiveData<Map<String, PredictionDataPoint>> predictionsHour = new MutableLiveData<>();


    public void loadData() {
        loadPredictions("1d", predictionsDay);
        loadPredictions("1h",predictionsHour);
    }


    private void loadPredictions(String interval, MutableLiveData<Map<String, PredictionDataPoint>> targetLiveData) {
        ApiClient.getInstance().getAllPredictions(interval, new DataCallback<Map<String, PredictionDataPoint>>() {
                    @Override
                    public void onSuccess(Map<String, PredictionDataPoint> data) {
                        targetLiveData.postValue(data);
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
    }


    public LiveData<Map<String, PredictionDataPoint>> getPrediction(String interval) {
        if ("1h".equals(interval)) return predictionsHour;
        else return predictionsDay;
    }


}
