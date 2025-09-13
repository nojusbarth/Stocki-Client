package com.example.stocki_client.remote;


import androidx.annotation.NonNull;

import com.example.stocki_client.model.stocks.StockDataPoint;
import com.example.stocki_client.prediction.PredictionDataPoint;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiClient {

    private static ApiClient instance;

    private OkHttpClient client;
    private final String baseURL = "http://10.0.2.2:5000";
    private final String historicalRequest = "%s/historical/%s?days=%d";
    private final String tickerListRequest = "%s/stocknames";
    private final String predictionRequest = "%s/predictions/%s?days=%d";

    private ApiClient() {
        client = new OkHttpClient();
    }


    public static ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    public void getTickerList(DataCallback callback) {

        String requestHTTP = String.format(tickerListRequest, baseURL);
        Request request = new Request.Builder()
                .url(requestHTTP)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonString = response.body().string();

                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<String>>() {}.getType();

                    List<String> stockTickers = gson.fromJson(jsonString, listType);

                    callback.onSuccess(stockTickers);
                } else {
                    callback.onError(new Exception("HTTP " + response.code()));
                }
            }
        });

    }

    public void getHistorical(String ticker, int days, DataCallback callback) {

        String requestHTTP = String.format(historicalRequest, baseURL, ticker, days);

        Request request = new Request.Builder()
                .url(requestHTTP)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonString = response.body().string();

                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<StockDataPoint>>() {
                    }.getType();
                    List<StockDataPoint> stockPoints = gson.fromJson(jsonString, listType);

                    callback.onSuccess(stockPoints);
                } else {
                    callback.onError(new Exception("HTTP " + response.code()));
                }
            }
        });

    }


    public void getPrediction(String ticker, int days, DataCallback callback) {

        String requestHTTP = String.format(predictionRequest, baseURL, ticker, days);
        Request request = new Request.Builder()
                .url(requestHTTP)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonString = response.body().string();

                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<PredictionDataPoint>>() {
                    }.getType();
                    List<PredictionDataPoint> predictionPoints = gson.fromJson(jsonString, listType);

                    callback.onSuccess(predictionPoints);
                } else {
                    callback.onError(new Exception("HTTP " + response.code()));
                }
            }
        });

    }

}
