package com.example.stocki_client.ui.mainpage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocki_client.R;
import com.example.stocki_client.prediction.PredictionSortingDataPoint;
import com.example.stocki_client.ui.stockdetail.PredictionAdapterStock;
import com.example.stocki_client.ui.stockdetail.ShowStockActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PredictionAdapterMain extends RecyclerView.Adapter<PredictionAdapterMain.StockViewHolder>{

    private List<PredictionSortingDataPoint> predictions;
    private String interval;
    private Context context;

    public PredictionAdapterMain(String interval, Context context) {
        predictions = new ArrayList<>();
        this.interval = interval;
        this.context = context;
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_prediction_mainpage, parent, false);
        return new PredictionAdapterMain.StockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {

        PredictionSortingDataPoint point = predictions.get(position);

        holder.txtStockName.setText(point.getName());

        holder.txtReturn.setText("Return: " + String.format(Locale.getDefault(), "%.2f", point.getPctReturn()) + "%");

        float risk = point.getRiskScore() / 100.0f;

        int color = ColorUtils.blendARGB(
                Color.GREEN,
                Color.RED,
                risk
        );
        holder.riskIndicator.getBackground().setTint(color);

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowStockActivity.class);

                String clickedStock = predictions.get(holder.getAdapterPosition()).getName();

                intent.putExtra("stockName", clickedStock);
                intent.putExtra("interval", interval);

                context.startActivity(intent);
            }
        });

    }

    public void updateData(List<PredictionSortingDataPoint> newPredictions) {
        predictions = new ArrayList<>(newPredictions);

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return predictions.size();
    }


    public class StockViewHolder extends RecyclerView.ViewHolder {

        private TextView txtReturn;
        private View riskIndicator;
        private TextView txtStockName;
        private CardView parent;

        public StockViewHolder(View view) {
            super(view);

            txtReturn = view.findViewById(R.id.txtPredictionReturnMainPage);
            txtStockName = view.findViewById(R.id.txtStockNameMainPage);
            riskIndicator = view.findViewById(R.id.viewRiskIndicatorMainPage);
            parent = view.findViewById(R.id.cvPredictionMainPage);
        }
    }
}
