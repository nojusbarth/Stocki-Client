package com.example.stocki_client.ui.mainpage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.stocki_client.R;
import com.example.stocki_client.ui.ClearableAutoCompleteTextView;
import com.example.stocki_client.ui.stock.ShowStockActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        viewModel.loadData();

        initStocks();
        initPager();

    }

    private void initPager() {
        TabLayout tabLayoutPredictions = findViewById(R.id.tabLayoutPredictions);
        ViewPager2 viewPagerPredictions = findViewById(R.id.viewPagerPredictions);

        PredictionPagerAdapter pagerAdapter = new PredictionPagerAdapter(this);
        viewPagerPredictions.setAdapter(pagerAdapter);

        viewPagerPredictions.setUserInputEnabled(false);

        new TabLayoutMediator(tabLayoutPredictions, viewPagerPredictions,
                (tab, position) -> {
                    if (position == PredictionPagerAdapter.POSITION_DAILY) {
                        tab.setText("Daily");
                    } else {
                        tab.setText("Hourly");
                    }
                }
        ).attach();
    }


    private void initStocks() {
        ClearableAutoCompleteTextView autoSearch = findViewById(R.id.stockSearch);

        viewModel.getPrediction("1d").observe(this, data -> {
            List<String> stockList = new ArrayList<>(data.keySet());

            ArrayAdapter<String> autoAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_dropdown_item_1line,
                    stockList
            );

            autoSearch.setAdapter(autoAdapter);
        });

        autoSearch.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);

            Intent intent = new Intent(this, ShowStockActivity.class);


            intent.putExtra("stockName", selected);
            intent.putExtra("interval", "1d");

            this.startActivity(intent);
        });
    }

    
}