package com.example.stocki_client.ui.mainpage.predictions;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.stocki_client.data.prediction.PredictionDataPoint;
import com.example.stocki_client.data.user.favorites.FavoritesRepository;
import com.example.stocki_client.data.user.portfolio.PortfolioData;
import com.example.stocki_client.remote.ApiClient;
import com.example.stocki_client.remote.DataCallback;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivityViewModel extends ViewModel {

    private final Map<String, MutableLiveData<Map<String, PredictionDataPoint>>> predictionMap = new HashMap<>();

    private final MutableLiveData<List<PortfolioData>> portfolios = new MutableLiveData<>();

    public MainActivityViewModel() {
        for (String interval : Arrays.asList("1h", "1d", "10d")) {
            predictionMap.put(interval, new MutableLiveData<>());
        }
    }


    public void loadData(String userId, LifecycleOwner owner) {
        FavoritesRepository.getInstance().init(
                predictionMap.get("1h"),
                predictionMap.get("1d"),
                owner
        );

        loadPredictions();
        loadPortfolios(userId);
        loadFavorites(userId);
    }


    private void loadPredictions() {
        ApiClient.getInstance().getAllPredictions(new DataCallback<Map<String, Map<String, PredictionDataPoint>>>() {
            @Override
            public void onSuccess(Map<String, Map<String, PredictionDataPoint>> data) {
                data.forEach((interval, map) -> {
                    MutableLiveData<Map<String, PredictionDataPoint>> liveData = predictionMap.get(interval);
                    if (liveData != null) liveData.postValue(map);
                });
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }


    private void loadPortfolios(String userId) {
        ApiClient.getInstance().getPortfolioData(userId, new DataCallback<List<PortfolioData>>() {
            @Override
            public void onSuccess(List<PortfolioData> data) {
                portfolios.postValue(data);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }


    private void loadFavorites(String userId) {
        ApiClient.getInstance().getFavorites(userId, new DataCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> data) {
                FavoritesRepository.getInstance().setFavorites(data);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }


    public LiveData<Map<String, PredictionDataPoint>> getPrediction(String interval) {
        return predictionMap.getOrDefault(interval, new MutableLiveData<>());
    }

    public LiveData<List<PortfolioData>> getPortfolios() {
        return portfolios;
    }
}
