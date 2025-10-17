package com.example.stocki_client.remote;


import androidx.annotation.NonNull;

import com.example.stocki_client.stocks.StockDataPoint;
import com.example.stocki_client.prediction.AccuracyDataPoint;
import com.example.stocki_client.prediction.PredictionDataPoint;
import com.example.stocki_client.models.ModelInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ApiClient {

    private static ApiClient instance;

    private final OkHttpClient client;
    private static final String baseURL = "http://62.169.27.233:5000";
    //private static final String baseURL = "http://10.0.2.2:5000";
    private static final String historicalRequest = "%s/historical/%s?period=%d&interval=%s";
    private static final String tickerListRequest = "%s/stocknames";
    private static final String predictionRequest = "%s/predictions/%s?period=%d&interval=%s";
    private static final String modelInfoRequest = "%s/modelinfo/%s?interval=%s";
    private static final String predictionsAllRequest = "%s/predictionsall/?interval=%s";
    private static final String accuracyRequest = "%s/accuracy/%s?period=%d&interval=%s";

    private ApiClient() {
        client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build();
    }


    public static ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    public void getTickerList(DataCallback<List<String>> callback) {

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

    public void getHistorical(String ticker, int days, String interval,
                              DataCallback<List<StockDataPoint>> callback) {

        String requestHTTP = String.format(historicalRequest, baseURL, ticker, days, interval);
        Request request = new Request.Builder()
                .url(requestHTTP)
                .header("Connection", "close")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                callback.onError(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody body = response.body()) {
                    if (body != null && response.isSuccessful()) {
                        String jsonString = body.string();
                        Type listType = new TypeToken<List<StockDataPoint>>() {}.getType();
                        List<StockDataPoint> stockPoints = new Gson().fromJson(jsonString, listType);
                        callback.onSuccess(stockPoints);
                    } else {
                        callback.onError(new Exception("HTTP " + response.code()));
                    }
                }
            }
        });
    }

    public void getPrediction(String ticker, int days, String interval,
                              DataCallback<List<PredictionDataPoint>> callback) {

        String requestHTTP = String.format(predictionRequest, baseURL, ticker, days, interval);
        Request request = new Request.Builder()
                .url(requestHTTP)
                .header("Connection", "close")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                callback.onError(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody body = response.body()) {
                    if (body != null && response.isSuccessful()) {
                        String jsonString = body.string();
                        Type listType = new TypeToken<List<PredictionDataPoint>>() {}.getType();
                        List<PredictionDataPoint> predictionPoints = new Gson().fromJson(jsonString, listType);
                        callback.onSuccess(predictionPoints);
                    } else {
                        callback.onError(new Exception("HTTP " + response.code()));
                    }
                }
            }
        });
    }

    public void getModelInfo(String ticker, String interval,
                             DataCallback<ModelInfo> callback) {

        String requestHTTP = String.format(modelInfoRequest, baseURL, ticker, interval);
        Request request = new Request.Builder()
                .url(requestHTTP)
                .header("Connection", "close")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                callback.onError(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody body = response.body()) {
                    if (body != null && response.isSuccessful()) {
                        String jsonString = body.string();
                        ModelInfo modelInfo = new Gson().fromJson(jsonString, ModelInfo.class);
                        callback.onSuccess(modelInfo);
                    } else {
                        callback.onError(new Exception("HTTP " + response.code()));
                    }
                }
            }
        });
    }

    public void getAllPredictions(String interval,
                                  DataCallback<Map<String, PredictionDataPoint>> callback) {

        String requestHTTP = String.format(predictionsAllRequest, baseURL, interval);
        Request request = new Request.Builder()
                .url(requestHTTP)
                .header("Connection", "close")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                callback.onError(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody body = response.body()) {
                    if (body != null && response.isSuccessful()) {
                        String jsonString = body.string();
                        Type mapType = new TypeToken<Map<String, PredictionDataPoint>>() {}.getType();
                        Map<String, PredictionDataPoint> predictionMap = new Gson().fromJson(jsonString, mapType);
                        callback.onSuccess(predictionMap);
                    } else {
                        callback.onError(new Exception("HTTP " + response.code()));
                    }
                }
            }
        });
    }

    public void getAccuracy(String ticker, int period, String interval,
                            DataCallback<Map<String, List<AccuracyDataPoint>>> callback) {

        String requestHTTP = String.format(accuracyRequest, baseURL, ticker, period, interval);
        Request request = new Request.Builder()
                .url(requestHTTP)
                .header("Connection", "close")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                callback.onError(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody body = response.body()) {
                    if (body != null && response.isSuccessful()) {
                        String jsonString = body.string();
                        Type mapType = new TypeToken<Map<String, List<AccuracyDataPoint>>>() {}.getType();
                        Map<String, List<AccuracyDataPoint>> accuracyMap = new Gson().fromJson(jsonString, mapType);
                        callback.onSuccess(accuracyMap);
                    } else {
                        callback.onError(new Exception("HTTP " + response.code()));
                    }
                }
            }
        });
    }


}
