package com.example.stocki_client.remote;


import androidx.annotation.NonNull;

import com.example.stocki_client.data.stocks.CatalogEntry;
import com.example.stocki_client.data.stocks.StockDataPoint;
import com.example.stocki_client.data.prediction.AccuracyDataPoint;
import com.example.stocki_client.data.prediction.PredictionDataPoint;
import com.example.stocki_client.data.models.ModelInfo;
import com.example.stocki_client.data.user.portfolio.PortfolioData;
import com.example.stocki_client.data.user.portfolio.StockPosition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ApiClient {

    private static ApiClient instance;

    private final OkHttpClient client;
    //private static final String baseURL = "http://62.169.27.233:5000";
    private static final String baseURL = "http://10.0.2.2:5000";
    private static final String historicalRequest = "%s/historical/%s?period=%d&interval=%s";
    private static final String predictionRequest = "%s/predictions/%s?interval=%s";
    private static final String modelInfoRequest = "%s/modelinfo/%s?interval=%s";
    private static final String predictionsAllRequest = "%s/predictionsall/?interval=%s";
    private static final String accuracyRequest = "%s/accuracy/%s?period=%d&interval=%s";
    private static final String createPortRequest = "%s/portfolio/create";
    private static final String deletePortRequest = "%s/portfolio/delete";
    private static final String addCashRequest = "%s/portfolio/addcash";
    private static final String portfolioRequest = "%s/portfolio/data/%s";
    private static final String catalogRequest = "%s/stockcatalog";
    private static final String portfolioStockUpdate = "%s/portfolio/modifystock";
    private static final String favoritesRequest = "%s/favorites/data/%s";
    private static final String favoritesUpdate = "%s/favorites/update";


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


    public void getHistorical(String ticker, int days,
                              DataCallback<Map<String, List<StockDataPoint>>> callback) {

        String requestHTTP = String.format(historicalRequest, baseURL, ticker, days, "all");
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
                        Type mapType = new TypeToken<Map<String, List<StockDataPoint>>>() {}.getType();
                        Map<String, List<StockDataPoint>> resultMap = new Gson().fromJson(jsonString, mapType);

                        callback.onSuccess(resultMap);
                    } else {
                        callback.onError(new Exception("HTTP " + response.code()));
                    }
                }
            }
        });
    }

    public void getPrediction(String ticker,
                              DataCallback<Map<String, List<PredictionDataPoint>>> callback) {

        String requestHTTP = String.format(predictionRequest, baseURL, ticker, "all");
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
                        Type mapType = new TypeToken<Map<String, List<PredictionDataPoint>>>() {}.getType();
                        Map<String, List<PredictionDataPoint>> resultMap = new Gson().fromJson(jsonString, mapType);

                        callback.onSuccess(resultMap);
                    } else {
                        callback.onError(new Exception("HTTP " + response.code()));
                    }
                }
            }
        });
    }



    public void getModelInfo(String ticker,
                             DataCallback<Map<String,ModelInfo>> callback) {

        String requestHTTP = String.format(modelInfoRequest, baseURL, ticker, "all");
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
                        Type mapType = new TypeToken<Map<String, ModelInfo>>() {}.getType();
                        Map<String, ModelInfo> resultMap = new Gson().fromJson(jsonString, mapType);

                        callback.onSuccess(resultMap);
                    } else {
                        callback.onError(new Exception("HTTP " + response.code()));
                    }
                }
            }
        });
    }

    public void getAllPredictions(DataCallback<Map<String,Map<String, PredictionDataPoint>>> callback) {

        String requestHTTP = String.format(predictionsAllRequest, baseURL, "all");
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
                        Type mapType = new TypeToken<Map<String,Map<String, PredictionDataPoint>>>() {}.getType();
                        Map<String,Map<String, PredictionDataPoint>> resultMap = new Gson().fromJson(jsonString, mapType);

                        callback.onSuccess(resultMap);
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


    public void createPortfolio(String userId, String name, String note) {
        String requestHTTP = String.format(createPortRequest,baseURL);
        JSONObject portfolioInfo = new JSONObject();

        try {
            portfolioInfo.put("user_id", userId);
            portfolioInfo.put("name", name);
            portfolioInfo.put("note", note);

        } catch (Exception e) {
            e.printStackTrace();
        }


        RequestBody body = RequestBody.create(
                portfolioInfo.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(requestHTTP)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String respBody = response.body() != null ? response.body().string() : "";
                    System.out.println("Server antwortet: " + respBody);
                } else {
                    System.out.println("Fehler: " + response.code());
                }
            }
        });
    }

    public void deletePortfolio(String userId, String portName) {
        String requestHTTP = String.format(deletePortRequest,baseURL);

        JSONObject portfolioInfo = new JSONObject();

        try {
            portfolioInfo.put("user_id", userId);
            portfolioInfo.put("name", portName);

        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(
                portfolioInfo.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(requestHTTP)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String respBody = response.body() != null ? response.body().string() : "";
                    System.out.println("Server antwortet: " + respBody);
                } else {
                    System.out.println("Fehler: " + response.code());
                }
            }
        });
    }

    public void addCash(String userId, String portfolioName, double amount) {
        String requestHTTP = String.format(addCashRequest,baseURL);

        JSONObject portfolioInfo = new JSONObject();

        try {
            portfolioInfo.put("user_id", userId);
            portfolioInfo.put("name", portfolioName);
            portfolioInfo.put("amount", amount);

        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(
                portfolioInfo.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(requestHTTP)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String respBody = response.body() != null ? response.body().string() : "";
                    System.out.println("Server antwortet: " + respBody);
                } else {
                    System.out.println("Fehler: " + response.code());
                }
            }
        });
    }


    public void getPortfolioData(String userId, DataCallback<List<PortfolioData>> callback) {

        String requestHTTP = String.format(portfolioRequest, baseURL, userId);
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
                        Type mapType = new TypeToken<List<PortfolioData>>() {}.getType();
                        List<PortfolioData> portfolios = new Gson().fromJson(jsonString, mapType);
                        callback.onSuccess(portfolios);
                    } else {
                        callback.onError(new Exception("HTTP " + response.code()));
                    }
                }
            }
        });
    }

    public void getCatalog(DataCallback<Map<String, CatalogEntry>> callback) {
        String requestHTTP = String.format(catalogRequest, baseURL);
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
                        Type mapType = new TypeToken<Map<String, CatalogEntry>>() {}.getType();
                        Map<String, CatalogEntry> catalog = new Gson().fromJson(jsonString, mapType);
                        callback.onSuccess(catalog);
                    } else {
                        callback.onError(new Exception("HTTP " + response.code()));
                    }
                }
            }
        });
    }

    public void updateStockPosition(String userId, String portfolioName, double newCash,
                                 StockPosition updatedPosition, Map<String, Double> newMetrics) {

        String requestHTTP = String.format(portfolioStockUpdate,baseURL);
        JSONObject portfolioInfo = new JSONObject();

        try {
            portfolioInfo.put("user_id", userId);
            portfolioInfo.put("name", portfolioName);
            portfolioInfo.put("cash", newCash);
            portfolioInfo.put("position", updatedPosition.toJson());
            portfolioInfo.put("metrics", new JSONObject(newMetrics));

        } catch (Exception e) {
            e.printStackTrace();
        }


        RequestBody body = RequestBody.create(
                portfolioInfo.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(requestHTTP)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String respBody = response.body() != null ? response.body().string() : "";
                    System.out.println("Server antwortet: " + respBody);
                } else {
                    System.out.println("Fehler: " + response.code());
                }
            }
        });
    }

    public void getFavorites(String userId, DataCallback<List<String>> callback) {
        String requestHTTP = String.format(favoritesRequest, baseURL, userId);
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
                        Type mapType = new TypeToken<List<String>>() {}.getType();
                        List<String> favorites = new Gson().fromJson(jsonString, mapType);
                        callback.onSuccess(favorites);
                    } else {
                        callback.onError(new Exception("HTTP " + response.code()));
                    }
                }
            }
        });
    }


    public void updateFavorites(String userId, String ticker) {

        String requestHTTP = String.format(favoritesUpdate,baseURL);
        JSONObject portfolioInfo = new JSONObject();

        try {
            portfolioInfo.put("user_id", userId);
            portfolioInfo.put("ticker", ticker);

        } catch (Exception e) {
            e.printStackTrace();
        }


        RequestBody body = RequestBody.create(
                portfolioInfo.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(requestHTTP)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String respBody = response.body() != null ? response.body().string() : "";
                    System.out.println("Server antwortet: " + respBody);
                } else {
                    System.out.println("Fehler: " + response.code());
                }
            }
        });
    }
}
