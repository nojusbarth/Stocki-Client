package com.example.stocki_client.ui.mainpage;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.stocki_client.R;
import com.example.stocki_client.remote.ApiClient;
import com.example.stocki_client.remote.DataCallback;
import com.example.stocki_client.ui.StockAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private StockAdapter stockAdapter;
    private EditText etxtSearch;
    private TabLayout tabLayoutPredictions;
    private ViewPager2 viewPagerPredictions;
    private PredictionPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initStocks();
        initViews();
        initPager();


    }

    private void initPager() {
        tabLayoutPredictions = findViewById(R.id.tabLayoutPredictions);
        viewPagerPredictions = findViewById(R.id.viewPagerPredictions);

        pagerAdapter = new PredictionPagerAdapter(this);
        viewPagerPredictions.setAdapter(pagerAdapter);

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


        ApiClient.getInstance().getTickerList(new DataCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> tickers) {
                runOnUiThread(() -> {
                    stockAdapter.updateData(tickers);
                });
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, "Fehler beim Laden", Toast.LENGTH_SHORT).show()
                );
            }
        });


    }




    private void initViews() {

        etxtSearch = findViewById(R.id.searchEditText);
        RecyclerView recStocks = findViewById(R.id.recstocks);
        stockAdapter = new StockAdapter(this);
        recStocks.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        recStocks.setAdapter(stockAdapter);



        //focus only when edittext used
        etxtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    recStocks.setVisibility(View.VISIBLE);
                } else if (etxtSearch.getText().toString().isEmpty()) {
                    recStocks.setVisibility(View.GONE);
                }
            }
        });


        //live filter for search
        etxtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                stockAdapter.filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

    }
}