package com.example.stocki_client.ui.stock;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocki_client.R;
import com.example.stocki_client.TimeFormatter;
import com.example.stocki_client.prediction.PredictionDataPoint;
import com.example.stocki_client.ui.stock.model.ModelInfoSheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PredictionAdapterStock extends RecyclerView.Adapter<PredictionAdapterStock.StockViewHolder>{

    private final List<PredictionDataPoint> predictions;
    private final String ticker;
    private String interval;
    private final AppCompatActivity activity;
    private final TimeFormatter timeFormatter;

    private final ShowStockViewModel viewModel;


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
        holder.txtReturn.setText(String.format(Locale.getDefault(), "%.2f%%", point.getPctReturn()));
        Context ctx = holder.itemView.getContext();
        if (point.getPctReturn() > 0.0) {
            holder.txtReturn.setTextColor(ContextCompat.getColor(ctx, R.color.green));
            holder.imgArrow.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.arrow_trending_up_static));
        } else {
            holder.txtReturn.setTextColor(ContextCompat.getColor(ctx, R.color.red));
            holder.imgArrow.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.arrow_trending_down_static));
        }

        float risk = point.getRiskScore() / 100.0f;

        int color = ColorUtils.blendARGB(
                Color.GREEN,
                Color.RED,
                risk
        );
        holder.riskIndicator.getBackground().setTint(color);

        holder.parent.setOnClickListener(v -> viewModel.getModelInfo(interval).observe(activity, modelInfo -> {
            if (modelInfo != null) {
                Map<String, Double> metrics = modelInfo.getMetrics();
                ModelInfoSheet sheet = ModelInfoSheet.newInstance(
                        modelInfo.getLatestUpdate(),
                        String.format(Locale.getDefault(),"%.2f", metrics.get("MAE")),
                        String.format(Locale.getDefault(),"%.2f", metrics.get("HitRate")),
                        String.format(Locale.getDefault(),"%.2f", metrics.get("Sharpe")),
                        String.format(Locale.getDefault(),"%.2f", metrics.get("MaxDrawdown")),
                        String.valueOf(point.getRiskScore()),
                        ticker,
                        interval,
                        position);
                sheet.show(activity.getSupportFragmentManager(), "ModelInfoBottomSheet");
            }
        }));
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

        private final TextView txtReturn;
        private final TextView txtDate;
        private final View riskIndicator;
        private final CardView parent;
        private final ImageView imgArrow;

        public StockViewHolder(View view) {
            super(view);

            txtReturn = view.findViewById(R.id.txtReturnValue);
            txtDate = view.findViewById(R.id.txtPredictionDateShowStock);
            riskIndicator = view.findViewById(R.id.viewRiskIndicatorShowStock);
            imgArrow = view.findViewById(R.id.imgReturnArrow);
            parent = view.findViewById(R.id.cvPredictionShowStock);
        }
    }
}
