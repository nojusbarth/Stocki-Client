package com.example.stocki_client.ui.mainpage.predictions;

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
        loadPredictions(predictionsHour, predictionsDay);
    }


    private void loadPredictions(MutableLiveData<Map<String, PredictionDataPoint>> targetLiveDataHour,
                                 MutableLiveData<Map<String, PredictionDataPoint>> targetLiveDataDay) {
        ApiClient.getInstance().getAllPredictions(new DataCallback<Map<String,Map<String, PredictionDataPoint>>>() {
                    @Override
                    public void onSuccess(Map<String,Map<String, PredictionDataPoint>> data) {
                        targetLiveDataHour.postValue(data.get("1h"));
                        targetLiveDataDay.postValue(data.get("1d"));
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
