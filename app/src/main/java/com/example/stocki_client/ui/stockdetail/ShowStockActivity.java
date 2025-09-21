package com.example.stocki_client.ui.stockdetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocki_client.R;
import com.example.stocki_client.model.stocks.StockDataPoint;
import com.example.stocki_client.prediction.PredictionDataPoint;
import com.example.stocki_client.remote.ApiClient;
import com.example.stocki_client.remote.DataCallback;
import com.github.mikephil.charting.charts.LineChart;

import java.util.List;

public class ShowStockActivity extends AppCompatActivity {

    private static final int PERIOD_PREDICTION = 3;
    private static final int PERIOD_HISTORICAL= 14;

    private String stockName;
    private String interval;

    private ApiClient client;
    private ChartBuilder chartBuilder;
    private PredictionAdapterStock predictionAdapterStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_stock);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        chartBuilder = new ChartBuilder();
        Intent intent = getIntent();

        client = ApiClient.getInstance();

        if(intent != null) {
            stockName = intent.getStringExtra("stockName");
            interval = intent.getStringExtra("interval");
        }

        createToolBar();
        initViews();
        initButtons();

        //order is important, fetch data after everything is initialized
        getHistorical();
        getPrediction();
    }




    private void initViews() {
        TextView txtTitle = findViewById(R.id.txtStockTitle);

        txtTitle.setText(stockName);

        RecyclerView recPreds = findViewById(R.id.recPredictions);

        predictionAdapterStock = new PredictionAdapterStock(this.stockName, this.interval, this);
        recPreds.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        recPreds.setAdapter(predictionAdapterStock);

    }


    private void createToolBar() {

        Toolbar toolbar = findViewById(R.id.toolBarShowStock);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //home button
        }

    }

    private void initButtons() {

        Button dailyButton = findViewById(R.id.btnTimeChangeDaily);
        Button hourlyButton = findViewById(R.id.btnTimeChangeHourly);

        dailyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!interval.equals("1d")) {
                    interval = "1d";
                    getHistorical();
                    getPrediction();

                    predictionAdapterStock.changeInterval(interval);
                }

            }
        });

        hourlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!interval.equals("1h")) {
                    interval = "1h";
                    getHistorical();
                    getPrediction();

                    predictionAdapterStock.changeInterval(interval);
                }
            }
        });

    }


    private void updateChart() {
        LineChart lineChart = findViewById(R.id.stockDataChart);
        chartBuilder.buildChart(lineChart);
    }

    private void getHistorical() {
        client.getHistorical(stockName, PERIOD_HISTORICAL, interval, new DataCallback<List<StockDataPoint>>() {
            @Override
            public void onSuccess(List<StockDataPoint> data) {
                chartBuilder.setHistorical(data);
                updateChart();
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(ShowStockActivity.this, "Fehler beim Laden", Toast.LENGTH_SHORT).show()
                );
            }
        });
    }


    private void getPrediction() {
        client.getPrediction(stockName, PERIOD_PREDICTION, interval, new DataCallback<List<PredictionDataPoint>>() {
            @Override
            public void onSuccess(List<PredictionDataPoint> data) {
                runOnUiThread(() -> {
                    chartBuilder.setPrediction(data);
                    updateChart();
                    predictionAdapterStock.updateData(data);
                });
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(ShowStockActivity.this, "Fehler beim Laden", Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}