package com.example.stocki_client.ui.mainpage.portfolio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.stocki_client.data.prediction.PredictionDataPoint;
import com.example.stocki_client.data.stocks.CatalogEntry;
import com.example.stocki_client.data.stocks.StockDataPoint;
import com.example.stocki_client.data.user.portfolio.PortfolioData;
import com.example.stocki_client.remote.ApiClient;
import com.example.stocki_client.remote.DataCallback;

import java.util.List;
import java.util.Map;

public class ShowPortfolioViewModel extends ViewModel {

    private final MutableLiveData<Map<String, CatalogEntry>> catalog = new MutableLiveData<>();

    public void loadData() {
        loadCatalog(catalog);
    }

    private void loadCatalog(MutableLiveData<Map<String, CatalogEntry>> target) {
        ApiClient.getInstance().getCatalog(new DataCallback<Map<String, CatalogEntry>>() {
            @Override
            public void onSuccess(Map<String, CatalogEntry> data) {
                catalog.postValue(data);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }


    public LiveData<Map<String, CatalogEntry>> getCatalog() {
        return catalog;
    }

}