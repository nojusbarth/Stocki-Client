package com.example.stocki_client.ui.mainpage.portfolio;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocki_client.R;
import com.example.stocki_client.data.stocks.CatalogEntry;
import com.example.stocki_client.data.user.portfolio.StockPosition;
import com.example.stocki_client.ui.stock.ShowStockActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder>{

    private List<StockPosition> positions;
    private OnStockSellListener listener;
    private Context context;

    public StockAdapter(Context context, OnStockSellListener listener) {
        positions = new ArrayList<>();
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_portfolio_stock, parent, false);
        return new StockAdapter.StockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {

        StockPosition stockPosition = positions.get(position);

        holder.txtStockName.setText(stockPosition.getTicker());

        holder.txtQuantityPrice.setText(String.format(Locale.getDefault(), "%.2f shares @ $%.2f",
                stockPosition.getQuantity(), stockPosition.getCurrentPrice()));

        Map<String, Double> stockMetrics = stockPosition.getMetrics();

        holder.txtAbsValue.setText(String.format(Locale.getDefault(), "%.2f$", stockMetrics.get("absValue")));
        holder.txtAbsReturn.setText(String.format(Locale.getDefault(), "%.2f$", stockMetrics.get("absReturn")));
        holder.txtPctReturn.setText(String.format(Locale.getDefault(), "%.0f%%", stockMetrics.get("pctReturn")));

        holder.txtBuyIn.setText(String.format(Locale.getDefault(), "%.2f$", stockPosition.getBuyPrice()));


        holder.imgSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.dialog_sell_stock, null);

                TextView txtStockName = dialogView.findViewById(R.id.txtStockNameSell);
                TextView txtStockValue = dialogView.findViewById(R.id.txtStockValueSell);
                TextInputLayout layoutSellAmount = dialogView.findViewById(R.id.layoutSellAmount);
                TextInputEditText etSellAmount = dialogView.findViewById(R.id.etSellAmount);
                Button btnAll = dialogView.findViewById(R.id.btnAllSell);

                txtStockName.setText(stockPosition.getTicker());
                txtStockValue.setText(String.format(Locale.getDefault(), "Positionswert: $%.2f", stockMetrics.get("absValue")));

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
                builder.setTitle("Aktie verkaufen")
                        .setView(dialogView)
                        .setNegativeButton("Abbrechen", (dialog, which) -> dialog.dismiss())
                        .setPositiveButton("Verkaufen", null);

                AlertDialog dialog = builder.create();
                dialog.show();

                // Button "Alles verkaufen" setzt Betrag automatisch
                btnAll.setOnClickListener(v -> {
                    etSellAmount.setText(String.format(Locale.getDefault(), "%.2f", stockMetrics.get("absValue")));
                });

                // Verkaufsklick
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                    String amountStr = etSellAmount.getText() != null ? etSellAmount.getText().toString().trim() : "";
                    boolean valid = true;
                    double amount = 0.0;

                    if (amountStr.isEmpty()) {
                        layoutSellAmount.setError("Bitte Verkaufsbetrag eingeben");
                        valid = false;
                    } else {
                        try {
                            amount = Double.parseDouble(amountStr);
                            layoutSellAmount.setError(null);
                        } catch (NumberFormatException e) {
                            layoutSellAmount.setError("Ungültige Zahl");
                            valid = false;
                        }
                    }

                    if (amount <= 0) {
                        layoutSellAmount.setError("Betrag muss größer als 0 sein");
                        valid = false;
                    } else if (amount > stockMetrics.get("absValue")) {
                        layoutSellAmount.setError("Betrag übersteigt Positionswert");
                        valid = false;
                    }

                    if (valid) {

                        if (listener != null) {
                            listener.onStockSoldCallback(stockPosition.getTicker(), amount, stockPosition.getCurrentPrice());
                        }
                        dialog.dismiss();

                    }
                });


            }
        });

        holder.imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowStockActivity.class);

                intent.putExtra("stockName", stockPosition.getTicker());
                intent.putExtra("interval","1d");

                context.startActivity(intent);
            }
        });

    }

    public void setData(List<StockPosition> newPositions) {
        positions.clear();
        for (StockPosition position : newPositions) {
            positions.add(position);
        }
        notifyDataSetChanged();
    }

    public void updatePosition(StockPosition toUpdate) {

        for (int i = 0; i < positions.size(); ++i) {

            if(positions.get(i).getTicker().equals(toUpdate.getTicker())) {

                if (toUpdate.getQuantity()  <= 1e-9 ) {
                    positions.remove(i);
                } else {
                    positions.set(i, new StockPosition(toUpdate));
                }
                notifyDataSetChanged();
                return;
            }

        }

        positions.add(toUpdate);

        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return positions.size();
    }


    public class StockViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtQuantityPrice;
        private final TextView txtStockName;
        private final TextView txtAbsReturn;
        private final TextView txtPctReturn;

        private final TextView txtAbsValue;

        private final TextView txtBuyIn;

        private final ImageView imgSell;
        private final ImageView imgInfo;


        public StockViewHolder(View view) {
            super(view);

            txtQuantityPrice = view.findViewById(R.id.txtPortfolioStockQuantityPrice);
            txtStockName = view.findViewById(R.id.txtPortfolioStockTicker);
            txtAbsValue = view.findViewById(R.id.txtPortfolioStockAbsValue);
            txtAbsReturn = view.findViewById(R.id.txtPortfolioStockAbsReturn);
            txtPctReturn = view.findViewById(R.id.txtPortfolioStockPctReturn);
            txtBuyIn = view.findViewById(R.id.txtPortfolioStockBuyIn);

            imgInfo = view.findViewById(R.id.btnPortfolioInfoStock);
            imgSell = view.findViewById(R.id.btnPortfolioSellStock);

        }
    }


}
