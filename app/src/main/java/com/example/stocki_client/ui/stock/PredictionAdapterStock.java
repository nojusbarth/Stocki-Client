package com.example.stocki_client.ui.stock;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocki_client.R;
import com.example.stocki_client.TimeFormatter;
import com.example.stocki_client.prediction.PredictionDataPoint;
import com.example.stocki_client.remote.ApiClient;
import com.example.stocki_client.remote.DataCallback;
import com.example.stocki_client.ui.stock.model.ModelInfo;
import com.example.stocki_client.ui.stock.model.ModelInfoSheet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PredictionAdapterStock extends RecyclerView.Adapter<PredictionAdapterStock.StockViewHolder>{

    private List<PredictionDataPoint> predictions;
    private String ticker;
    private String interval;
    private AppCompatActivity activity;
    private TimeFormatter timeFormatter;

    private ShowStockViewModel viewModel;


    public PredictionAdapterStock(String ticker, String interval, AppCompatActivity activity, ShowStockViewModel vm) {
        predictions = new ArrayList<>();
        this.ticker = ticker;
        this.interval = interval;
        this.activity = activity;
        this.timeFormatter = new TimeFormatter();
        viewModel= vm;
    }

    @NonNull
    @Override
    public PredictionAdapterStock.StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_prediction_showstock, parent, false);
        return new PredictionAdapterStock.StockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PredictionAdapterStock.StockViewHolder holder, int position) {
        PredictionDataPoint point = predictions.get(position);

        String rawDate = point.getDate();

        String formatted = timeFormatter.formatCV(rawDate, interval);
        holder.txtDate.setText(formatted);
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
                viewModel.getModelInfo(interval).observe(activity, modelInfo -> {
                    if (modelInfo != null) {
                        Map<String, Double> metrics = modelInfo.getMetrics();
                        String metricsString = "MAE: " + String.format("%.2f", metrics.get("MAE")) + "$" +
                                               " RMSE: " + String.format("%.2f", metrics.get("RMSE")) + "$" +
                                               " R2: " + String.format("%.2f", metrics.get("R2"));

                        ModelInfoSheet sheet = ModelInfoSheet.newInstance(
                                modelInfo.getLatestUpdate(),
                                String.valueOf(modelInfo.getNumSamples()),
                                metricsString,
                                String.valueOf(point.getRiskScore()),
                                ticker,
                                interval);
                        sheet.show(activity.getSupportFragmentManager(), "ModelInfoBottomSheet");
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return predictions.size();
    }


    public void updateData(List<PredictionDataPoint> newData) {
        predictions.clear();
        predictions.addAll(newData);

        notifyDataSetChanged();
    }

    public void changeInterval(String interval) {
        this.interval = interval;

        notifyDataSetChanged();
    }


    public class StockViewHolder extends RecyclerView.ViewHolder {

        private TextView txtReturn;
        private TextView txtDate;
        private View riskIndicator;
        private CardView parent;

        public StockViewHolder(View view) {
            super(view);

            txtReturn = view.findViewById(R.id.txtPredictionReturnShowStock);
            txtDate = view.findViewById(R.id.txtPredictionDateShowStock);
            riskIndicator = view.findViewById(R.id.viewRiskIndicatorShowStock);
            parent = view.findViewById(R.id.cvPredictionShowStock);
        }
    }
}
