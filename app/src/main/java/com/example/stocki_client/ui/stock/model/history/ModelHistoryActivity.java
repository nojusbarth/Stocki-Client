package com.example.stocki_client.ui.stock.model.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.stocki_client.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ModelHistoryActivity extends AppCompatActivity {

    private String stockName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_model_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();

        String interval = "";
        int step = -1;
        if(intent != null) {
            stockName = intent.getStringExtra("stockName");
            interval = intent.getStringExtra("interval");
            step = intent.getIntExtra("step",-1);
        }

        ModelHistoryViewModel viewModel = new ViewModelProvider(this).get(ModelHistoryViewModel.class);

        viewModel.initData(stockName, interval);

        createToolbar();
        initPager(step, interval);
    }


    private void initPager(int step, String interval) {
        TabLayout tabLayoutPredictions = findViewById(R.id.tabLayoutPredictionsHistory);
        ViewPager2 viewPagerPredictions = findViewById(R.id.viewPagerPredictionsHistory);

        int numberOfSteps;
        if ("10d".equals(interval)) {
            numberOfSteps = 1;
        } else if ("1h".equals(interval) || "1d".equals(interval)) {
            numberOfSteps = 3;
        } else {
            numberOfSteps = 0;
        }

        HistoryPagerAdapter pagerAdapter = new HistoryPagerAdapter(this, numberOfSteps);
        viewPagerPredictions.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayoutPredictions, viewPagerPredictions,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText(getResources().getString(R.string.label_one_ahead));
                            break;
                        case 1:
                            tab.setText(getResources().getString(R.string.label_two_ahead));
                            break;
                        case 2:
                            tab.setText(getResources().getString(R.string.label_three_ahead));
                            break;
                    }
                }
        ).attach();

        viewPagerPredictions.setCurrentItem(Math.min(step, numberOfSteps - 1), false);
    }





    private void createToolbar() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.modelHistoryToolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Model-History");
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}