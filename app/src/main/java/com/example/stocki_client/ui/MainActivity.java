package com.example.stocki_client.ui;

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

import com.example.stocki_client.R;
import com.example.stocki_client.model.stocks.StockManager;
import com.example.stocki_client.remote.ApiClient;
import com.example.stocki_client.remote.DataCallback;
import com.example.stocki_client.ui.stockdetail.ShowStockActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private StockAdapter stockAdapter;
    private EditText etxtSearch;
    private StockManager stockManager;

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



    }


    private void initStocks() {

        //empty manager as long as no stocks are loaded
        stockManager = new StockManager(new ArrayList<>());

        ApiClient.getInstance().getTickerList(new DataCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> tickers) {
                runOnUiThread(() -> {
                    stockManager = new StockManager(tickers);
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