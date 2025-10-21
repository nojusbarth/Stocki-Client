package com.example.stocki_client.ui.mainpage.portfolio;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocki_client.R;
import com.example.stocki_client.data.stocks.CatalogEntry;
import com.example.stocki_client.data.user.UserIdManager;
import com.example.stocki_client.data.user.portfolio.PortfolioData;
import com.example.stocki_client.data.user.portfolio.StockPosition;
import com.example.stocki_client.remote.ApiClient;
import com.example.stocki_client.ui.ClearableAutoCompleteTextView;
import com.example.stocki_client.ui.mainpage.predictions.MainActivityViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ShowPortfolioActivity extends AppCompatActivity {

    private ShowPortfolioViewModel viewModel;

    private Map<String, CatalogEntry> catalog;

    private PortfolioData portfolioData;

    private StockAdapter stockAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_portfolio);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        viewModel = new ViewModelProvider(this).get(ShowPortfolioViewModel.class);
        viewModel.loadData();
        catalog = new HashMap<>();

        viewModel.getCatalog().observe(this, data -> {
            catalog = new HashMap<>(data);
        });

        portfolioData = getIntent().getParcelableExtra("portfolioData");

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("updatedPortfolio", portfolioData);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });


        createToolBar();

        initHeader();
        initAddStocks();
        initStocks();
    }




    private void createToolBar() {

        Toolbar toolbar = findViewById(R.id.toolBarShowPortfolio);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }


    private void initHeader() {
        Map<String, Double> portfolioMetrics = portfolioData.getMetrics();

        TextView txtName = findViewById(R.id.txtPortfolioHeaderName);
        txtName.setText(portfolioData.getName());


        TextView txtValue = findViewById(R.id.txtPortfolioValueHeader);
        txtValue.setText(String.format(Locale.getDefault(),"%.2f$",portfolioMetrics.get("absValue")));

        TextView txtAbsReturn = findViewById(R.id.txtPortfolioAbsReturnHeader);
        double absReturn = portfolioMetrics.get("absReturn");
        txtAbsReturn.setText(String.format(Locale.getDefault(),"%.2f$", absReturn));

        if (absReturn >= 0.0) {
            txtAbsReturn.setTextColor(getColor(R.color.green));
        } else {
            txtAbsReturn.setTextColor(getColor(R.color.red));
        }


        TextView txtRelReturn = findViewById(R.id.txtPortfolioPctReturnHeader);
        double relReturn = portfolioMetrics.get("pctReturn");
        txtRelReturn.setText(String.format(Locale.getDefault(),"%.0f%%", relReturn));

        ImageView trendView = findViewById(R.id.imgPortfolioTrendHeader);

        if (relReturn >= 0.0) {
            txtRelReturn.setTextColor(getColor(R.color.green));
            trendView.setImageResource(R.drawable.arrow_trending_up_animated);

        } else {
            txtRelReturn.setTextColor(getColor(R.color.red));
            trendView.setImageResource(R.drawable.arrow_trending_down_animated);

        }

        Drawable drawable = trendView.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }


        TextView txtCash = findViewById(R.id.txtPortfolioCashHeader);
        txtCash.setText(String.format(Locale.getDefault(),"Cash: %.2f$", portfolioData.getCash()));



        ImageView imgNote = findViewById(R.id.imgPortfolioNoteHeader);

        imgNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PortfolioInfoSheet sheet = PortfolioInfoSheet.newInstance(
                        portfolioData.getCreatedAt(),
                        portfolioData.getNote());
                sheet.show(getSupportFragmentManager(), "PortfolioInfoBottomSheet");
            }
        });

        Button btnAddCash = findViewById(R.id.btnAddCash);

        btnAddCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCashDialog();
            }
        });
    }

    private void initStocks() {
        RecyclerView recView = findViewById(R.id.rvStocks);

        recView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        stockAdapter = new StockAdapter(this, new OnStockSellListener() {
            @Override
            public void onStockSoldCallback(String ticker, double sellAmount, double sellPrice) {
                onStockSold(ticker,sellAmount,sellPrice);
            }
        });
        stockAdapter.setData(portfolioData.getStockPositions());

        recView.setAdapter(stockAdapter);
    }

    private void showAddCashDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_cash, null);
        TextInputLayout layoutCash = dialogView.findViewById(R.id.layoutCash);
        TextInputEditText etCash = dialogView.findViewById(R.id.etCash);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Cash hinzufügen")
                .setView(dialogView)
                .setNegativeButton("Abbrechen", (d, which) -> d.dismiss())
                .setPositiveButton("Hinzufügen", null)
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btnPositive.setOnClickListener(view -> {
                String cashInput = etCash.getText() != null ? etCash.getText().toString().trim() : "";
                boolean valid = true;

                if (cashInput.isEmpty()) {
                    layoutCash.setError("Bitte einen Betrag eingeben");
                    valid = false;
                } else {
                    try {
                        int cashValue = Integer.parseInt(cashInput);

                        if (cashValue <= 0) {
                            layoutCash.setError("Betrag muss größer als 0 sein");
                            valid = false;
                        } else if (cashInput.length() > 7) {
                            layoutCash.setError("Maximal 7-stellig erlaubt");
                            valid = false;
                        } else {
                            layoutCash.setError(null);

                            onAddCash(cashValue);

                            dialog.dismiss();
                        }

                    } catch (NumberFormatException e) {
                        layoutCash.setError("Ungültige Zahl");
                        valid = false;
                    }
                }
            });
        });

        dialog.show();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(
                    (int)(getResources().getDisplayMetrics().widthPixels * 0.85),
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
        }
    }


    private void onAddCash(double value) {

        ApiClient.getInstance().addCash(
                UserIdManager.getInstance(this).getUserId(),
                portfolioData.getName(),
                value
        );

        portfolioData.setCash(portfolioData.getCash() + value);

        TextView txtCash = findViewById(R.id.txtPortfolioCashHeader);
        txtCash.setText(String.format(Locale.getDefault(),"Cash: %.2f$", portfolioData.getCash()));

    }



    private void initAddStocks() {
        Button btnAddStock = findViewById(R.id.btnAddStockPortfolio);

        btnAddStock.setOnClickListener(v -> {
            LayoutInflater inflater = LayoutInflater.from(ShowPortfolioActivity.this);
            View dialogView = inflater.inflate(R.layout.dialog_add_stock_portfolio, null);

            ClearableAutoCompleteTextView stockSearch = dialogView.findViewById(R.id.stockSearch);
            TextView txtCash = dialogView.findViewById(R.id.txtCash);
            TextInputLayout layoutAmount = dialogView.findViewById(R.id.layoutBuyAmount);
            TextInputEditText etBuyAmount = dialogView.findViewById(R.id.etBuyAmount);

            txtCash.setText(String.format(Locale.getDefault(), "Cash verfügbar: $%.2f", portfolioData.getCash()));

            List<String> stockNames = new ArrayList<>(catalog.keySet());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(ShowPortfolioActivity.this,
                    android.R.layout.simple_dropdown_item_1line, stockNames);
            stockSearch.setAdapter(adapter);

            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ShowPortfolioActivity.this);
            builder.setTitle("Aktie hinzufügen")
                    .setView(dialogView)
                    .setNegativeButton("Abbrechen", (dialog, which) -> dialog.dismiss())
                    .setPositiveButton("Hinzufügen", null);

            AlertDialog dialog = builder.create();
            dialog.show();

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v2 -> {
                String selectedName = stockSearch.getText().toString().trim();
                String amountStr = etBuyAmount.getText() != null ? etBuyAmount.getText().toString().trim() : "";

                boolean valid = true;

                if (selectedName.isEmpty() || !catalog.containsKey(selectedName)) {
                    stockSearch.setError("Bitte eine gültige Aktie auswählen");
                    valid = false;
                } else {
                    stockSearch.setError(null);
                }

                double amount = 0;
                if (amountStr.isEmpty()) {
                    layoutAmount.setError("Bitte Betrag eingeben");
                    valid = false;
                } else {
                    try {
                        amount = Double.parseDouble(amountStr);
                        layoutAmount.setError(null);
                    } catch (NumberFormatException e) {
                        layoutAmount.setError("Ungültige Zahl");
                        valid = false;
                    }
                }

                if (amount <= 0) {
                    layoutAmount.setError("Betrag muss größer als 0 sein");
                    valid = false;
                }

                CatalogEntry entry = catalog.get(selectedName);
                if (valid && entry != null) {
                    if (amount > portfolioData.getCash()) {
                        layoutAmount.setError("Nicht genug Cash für diesen Kauf");
                        valid = false;
                    } else {
                        layoutAmount.setError(null);
                    }
                }

                if (valid && entry != null) {
                    double currentPrice = entry.getCurrentPrice();

                    onStockAdded(selectedName, amount, currentPrice);

                    dialog.dismiss();
                }
            });
        });


    }


    public void onStockAdded(String selectedStock, double amount, double currentPrice) {

        StockPosition updatedPosition = portfolioData.buyStock(selectedStock, amount, currentPrice);

        double currentCash = portfolioData.getCash();


        Map<String, Double> newMetrics = portfolioData.getMetrics();

        ApiClient.getInstance().updateStockPosition(UserIdManager.getInstance(this).getUserId(),
                portfolioData.getName(), currentCash, updatedPosition, newMetrics);

        stockAdapter.updatePosition(updatedPosition);
        initHeader();
    }

    public void onStockSold(String ticker, double amount, double currentPrice) {

        StockPosition updatedPosition = portfolioData.sellStock(ticker, amount, currentPrice);

        if(updatedPosition == null)
        {
            updatedPosition = new StockPosition(ticker,0.0,0.0);
        }

        double currentCash = portfolioData.getCash();
        Map<String, Double> newMetrics = portfolioData.getMetrics();

        ApiClient.getInstance().updateStockPosition(UserIdManager.getInstance(this).getUserId(),
                portfolioData.getName(), currentCash, updatedPosition, newMetrics);
        stockAdapter.updatePosition(updatedPosition);


        initHeader();
    }


    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}