package com.example.stocki_client.remote;


import androidx.annotation.NonNull;

import com.example.stocki_client.model.stocks.StockDataPoint;
import com.example.stocki_client.prediction.AccuracyDataPoint;
import com.example.stocki_client.prediction.PredictionDataPoint;
import com.example.stocki_client.ui.stock.model.ModelInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiClient {

    private static ApiClient instance;

    private OkHttpClient client;
    private final String baseURL = "http://192.168.178.47:5000";
    private final String historicalRequest = "%s/historical/%s?period=%d&interval=%s";
    private final String tickerListRequest = "%s/stocknames";
    private final String predictionRequest = "%s/predictions/%s?period=%d&interval=%s";
    private final String modelInfoRequest = "%s/modelinfo/%s?interval=%s";
    private final String predictionsAllRequest = "%s/predictionsall/?interval=%s";
    private final String accuracyRequest = "%s/accuracy/%s?period=%d&interval=%s";

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

    public void getHistorical(String ticker, int days, String interval, DataCallback callback) {

        String requestHTTP = String.format(historicalRequest, baseURL, ticker, days, interval);

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


    public void getPrediction(String ticker, int days, String interval, DataCallback callback) {

        String requestHTTP = String.format(predictionRequest, baseURL, ticker, days, interval);
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

    public void getModelInfo(String ticker, String interval, DataCallback callback) {
        String requestHTTP = String.format(modelInfoRequest, baseURL, ticker, interval);
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
                    ModelInfo modelInfo = gson.fromJson(jsonString, ModelInfo.class);

                    callback.onSuccess(modelInfo);
                } else {
                    callback.onError(new Exception("HTTP " + response.code()));
                }
            }
        });
    }

    public void getAllPredictions(String interval, DataCallback callback) {
        String requestHTTP = String.format(predictionsAllRequest, baseURL, interval);
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
                    Type mapType = new TypeToken<Map<String, PredictionDataPoint>>(){}.getType();

                    Map<String, PredictionDataPoint> predictionMap = gson.fromJson(jsonString, mapType);

                    callback.onSuccess(predictionMap);

                } else {
                    callback.onError(new Exception("HTTP " + response.code()));
                }
            }
        });
    }


    public void getAccuracy(String ticker, int period, String interval, DataCallback callback) {

        String requestHTTP = String.format(accuracyRequest, baseURL, ticker, period, interval);
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
                    Type mapType = new TypeToken<Map<String, List<AccuracyDataPoint>>>(){}.getType();

                    Map<Integer, List<AccuracyDataPoint>> accuracyMap = gson.fromJson(jsonString, mapType);

                    callback.onSuccess(accuracyMap);

                } else {
                    callback.onError(new Exception("HTTP " + response.code()));
                }
            }
        });


    }

}
