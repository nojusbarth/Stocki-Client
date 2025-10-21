package com.example.stocki_client.ui.mainpage.predictions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.stocki_client.data.prediction.PredictionDataPoint;
import com.example.stocki_client.data.user.portfolio.PortfolioData;
import com.example.stocki_client.remote.ApiClient;
import com.example.stocki_client.remote.DataCallback;

import java.util.List;
import java.util.Map;

public class MainActivityViewModel extends ViewModel {

    private final MutableLiveData<Map<String, PredictionDataPoint>> predictionsDay = new MutableLiveData<>();
    private final MutableLiveData<Map<String, PredictionDataPoint>> predictionsHour = new MutableLiveData<>();
    private final MutableLiveData<List<PortfolioData>> portfolios = new MutableLiveData<>();

    public void loadData(String userId) {
        loadPredictions(predictionsHour, predictionsDay);
        loadPortfolios(userId, portfolios);
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

    private void loadPortfolios(String userID, MutableLiveData<List<PortfolioData>> target) {
        ApiClient.getInstance().getPortfolioData(userID, new DataCallback<List<PortfolioData>>() {
            @Override
            public void onSuccess(List<PortfolioData> data) {
                target.postValue(data);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    public LiveData<List<PortfolioData>> getPortfolios() {
        return portfolios;
    }

}
