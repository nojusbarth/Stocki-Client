package com.example.stocki_client.ui.stock;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.stocki_client.R;
import com.example.stocki_client.data.user.favorites.FavoritesRepository;
import com.example.stocki_client.ui.WrapContentRecyclerView;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;

public class ShowStockActivity extends AppCompatActivity {

    private ShowStockViewModel viewModel;
    private PredictionChartBuilder chartBuilder;
    private PredictionAdapterStock predictionAdapterStock;

    private String interval;
    private String stockName;
    private WrapContentRecyclerView recPreds;

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
        viewModel = new ViewModelProvider(this).get(ShowStockViewModel.class);
        Intent intent = getIntent();

        if(intent != null) {
            stockName = intent.getStringExtra("stockName");
            interval = intent.getStringExtra("interval");
        }

        chartBuilder = new PredictionChartBuilder(interval);

        createToolBar();
        initViews();
        initButtons();
        initFavorite();

        viewModel.loadData(stockName);

        viewModel.getHistorical(interval).observe(this, data -> {
            chartBuilder.setHistorical(new ArrayList<>(data));
            updateChart();
        });

        viewModel.getPrediction(interval).observe(this,data -> {
            chartBuilder.setPrediction(new ArrayList<>(data));
            updateChart();
            predictionAdapterStock.updateData(new ArrayList<>(data));
            recPreds.scheduleLayoutAnimation();
        });

    }




    private void initViews() {
        TextView txtTitle = findViewById(R.id.txtStockTitle);

        txtTitle.setText(stockName);

        recPreds = findViewById(R.id.recPredictions);

        predictionAdapterStock = new PredictionAdapterStock(this.stockName, this.interval, this, viewModel);
        recPreds.setAdapter(predictionAdapterStock);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recPreds.setLayoutManager(layoutManager);

    }


    private void createToolBar() {

        Toolbar toolbar = findViewById(R.id.toolBarShowStock);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    private void initButtons() {

        Button dailyButton = findViewById(R.id.btnTimeChangeDaily);
        Button hourlyButton = findViewById(R.id.btnTimeChangeHourly);

        dailyButton.setOnClickListener(v -> {

            if (!interval.equals("1d")) {
                interval = "1d";
                onIntervalChange();
            }

        });

        hourlyButton.setOnClickListener(v -> {
            if(!interval.equals("1h")) {
                interval = "1h";
                onIntervalChange();
            }
        });

    }

    private void initFavorite() {
        ImageButton btnFavorite = findViewById(R.id.btnFavoriteToggle);

        btnFavorite.setSelected(FavoritesRepository.getInstance().isFavorite(stockName));

        btnFavorite.setOnClickListener(v -> {
            boolean selected = !btnFavorite.isSelected();
            btnFavorite.setSelected(selected);

            Animation anim = AnimationUtils.loadAnimation(v.getContext(), R.anim.scale_up);
            v.startAnimation(anim);

            if (selected) {
                Toast.makeText(v.getContext(), stockName + " wurde hinzugefügt", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(v.getContext(), stockName + " wurde entfernt", Toast.LENGTH_SHORT).show();
            }

            FavoritesRepository.getInstance().toggleFavorite(stockName, this);
        });
    }


    private void updateChart() {
        LineChart lineChart = findViewById(R.id.stockDataChart);
        chartBuilder.buildChart(lineChart);
    }

    private void onIntervalChange() {
        if (viewModel.getPrediction(interval).getValue() != null) {
            predictionAdapterStock.updateData(new ArrayList<>(viewModel.getPrediction(interval).getValue()));
            chartBuilder.setPrediction(new ArrayList<>(viewModel.getPrediction(interval).getValue()));
        }
        if(viewModel.getHistorical(interval).getValue() != null) {
            predictionAdapterStock.updateData(new ArrayList<>(viewModel.getPrediction(interval).getValue()));
            chartBuilder.setHistorical(new ArrayList<>(viewModel.getHistorical(interval).getValue()));
        }
        predictionAdapterStock.changeInterval(interval);
        chartBuilder.setInterval(interval);
        recPreds.scheduleLayoutAnimation();
        updateChart();
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