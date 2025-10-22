package com.example.stocki_client.data.user.favorites;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.stocki_client.data.prediction.PredictionDataPoint;
import com.example.stocki_client.data.user.UserIdManager;
import com.example.stocki_client.remote.ApiClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FavoritesRepository {

    private static FavoritesRepository instance;

    private MutableLiveData<Map<String, PredictionDataPoint>> predictionsHour;
    private MutableLiveData<Map<String, PredictionDataPoint>> predictionsDay;

    private final MutableLiveData<List<FavoriteDisplayData>> favorites = new MutableLiveData<>(new ArrayList<>());
    private final List<String> favoriteTickers = new ArrayList<>();

    private FavoritesRepository() {}

    public static synchronized FavoritesRepository getInstance() {
        if (instance == null) instance = new FavoritesRepository();
        return instance;
    }

    public void init(
            MutableLiveData<Map<String, PredictionDataPoint>> predictionsHour,
            MutableLiveData<Map<String, PredictionDataPoint>> predictionsDay,
            LifecycleOwner owner
    ) {
        this.predictionsHour = predictionsHour;
        this.predictionsDay = predictionsDay;

        predictionsHour.observe(owner, map -> tryRebuild());
        predictionsDay.observe(owner, map -> tryRebuild());
    }

    private void tryRebuild() {
        Map<String, PredictionDataPoint> hourMap = predictionsHour.getValue();
        Map<String, PredictionDataPoint> dayMap = predictionsDay.getValue();

        if (hourMap != null && !hourMap.isEmpty() && dayMap != null && !dayMap.isEmpty()) {
            rebuildFavorites();
        }
    }

    public void setFavorites(List<String> tickers) {
        favoriteTickers.clear();
        if (tickers != null) {
            favoriteTickers.addAll(tickers);
        }
        rebuildFavorites();
    }

    public LiveData<List<FavoriteDisplayData>> getFavorites() {
        return favorites;
    }

    public void toggleFavorite(String ticker, Context context) {
        if (favoriteTickers.contains(ticker)) {
            favoriteTickers.remove(ticker);
        } else {
            favoriteTickers.add(ticker);
        }

        ApiClient.getInstance().updateFavorites(UserIdManager.getInstance(context).getUserId(),
                ticker);

        rebuildFavorites();
    }

    public boolean isFavorite(String ticker) {

        for (String tickerFav : favoriteTickers) {
            if (tickerFav.equals(ticker)) {
                return true;
            }
        }
        return false;
    }

    private void rebuildFavorites() {
        if (predictionsHour == null || predictionsDay == null) return;

        Map<String, PredictionDataPoint> hourMap = predictionsHour.getValue();
        Map<String, PredictionDataPoint> dayMap = predictionsDay.getValue();

        if (hourMap == null || dayMap == null) return;


        List<FavoriteDisplayData> combined = new ArrayList<>();
        for (String ticker : favoriteTickers) {
            PredictionDataPoint h = hourMap.get(ticker);
            PredictionDataPoint d = dayMap.get(ticker);
            combined.add(new FavoriteDisplayData(ticker, h, d));
        }

        favorites.postValue(combined);
    }
}


