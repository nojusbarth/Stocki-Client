package com.example.stocki_client.stocks;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.stocki_client.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class ShowStockActivity extends AppCompatActivity {


    private String stockName;

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


        Intent intent = getIntent();

        if(intent != null) {
            stockName = intent.getStringExtra("stockName");
        }


        createToolBar();
        initViews();

        buildChart();

    }




    private void initViews() {
        TextView txtTitle = findViewById(R.id.txtStockTitle);

        txtTitle.setText(stockName);
    }


    private void createToolBar() {

        Toolbar toolbar = findViewById(R.id.toolBarShowStock);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //home button
        }

    }


    private void buildChart() {
        LineChart lineChart = findViewById(R.id.stockDataChart);

        List<Integer> x = List.of(10,20,30,40,50);
        List<Integer> y = List.of(15,25,10,11,17);

        List<Integer> yPred = List.of(20,40,50);


        List<Entry> historical = new ArrayList<>();
        for (int i = 0; i < x.size(); i++) {
            historical.add(new Entry(i, y.get(i))); // x = Index, y = Wert
        }

        List<Entry> prediction = new ArrayList<>();
        for (int i = 0; i < yPred.size(); i++) {
            prediction.add(new Entry(x.size() - 1 + i, yPred.get(i)));
        }

        LineDataSet setHistorical = new LineDataSet(historical, "Historisch");
        setHistorical.setColor(Color.BLACK);
        setHistorical.setLineWidth(2f);
        setHistorical.setDrawCircles(false);

        LineDataSet setPrediction = new LineDataSet(prediction, "Vorhersage");
        setPrediction.setColor(Color.RED);
        setPrediction.enableDashedLine(10f, 5f, 0f);
        setPrediction.setLineWidth(2f);
        setPrediction.setDrawCircles(false);

        LineData lineData = new LineData(setHistorical, setPrediction);
        lineChart.setData(lineData);

        lineChart.getDescription().setText("Close über Zeit");
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getAxisRight().setEnabled(false); // nur linke Y-Achse
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.invalidate(); // neu rendern
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