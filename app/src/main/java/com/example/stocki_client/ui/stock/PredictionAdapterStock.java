package com.example.stocki_client.ui.stock;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocki_client.R;
import com.example.stocki_client.TimeFormatter;
import com.example.stocki_client.data.prediction.PredictionDataPoint;
import com.example.stocki_client.ui.mainpage.predictions.PredictionAdapterMain;
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


        if(point.getType().equals("interval")) {
            setReturnInterval(holder, point);
        } else if(point.getType().equals("point")) {
            setReturnPoint(holder, point);
        }

        float confidence = point.getConfidence() / 100.0f;

        int color = ColorUtils.blendARGB(
                Color.RED,
                Color.GREEN,
                confidence
        );
        holder.confidenceIndicator.getBackground().setTint(color);

        holder.parent.setOnClickListener(v -> viewModel.getModelInfo(interval).observe(activity, modelInfo -> {
            if (modelInfo != null) {

                ModelInfoSheet sheet = ModelInfoSheet.newInstance(
                        modelInfo.getLatestUpdate(),
                        modelInfo.getPredictionType(),
                        String.valueOf(point.getConfidence()),
                        ticker,
                        interval,
                        position,
                        modelInfo.getMetrics());

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

    private void setReturnPoint(StockViewHolder holder, PredictionDataPoint point) {
        holder.txtReturn.setVisibility(View.VISIBLE);
        holder.imgArrow.setVisibility(View.VISIBLE);
        holder.layoutReturnInterval.setVisibility(View.GONE);

        holder.txtReturn.setText(String.format(Locale.getDefault(), "%.2f%%", point.getPctReturn()));

        if (point.getPctReturn() > 0.0) {
            holder.txtReturn.setTextColor(ContextCompat.getColor(activity, R.color.green));

            holder.imgArrow.setImageResource(R.drawable.arrow_trending_up_animated);

        } else {
            holder.txtReturn.setTextColor(ContextCompat.getColor(activity, R.color.red));

            holder.imgArrow.setImageResource(R.drawable.arrow_trending_down_animated);
        }

        Drawable drawable = holder.imgArrow.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }

    private void setReturnInterval(StockViewHolder holder, PredictionDataPoint point) {
        holder.txtReturn.setVisibility(View.GONE);
        holder.imgArrow.setVisibility(View.GONE);
        holder.layoutReturnInterval.setVisibility(View.VISIBLE);

        float bottom = point.getIntervalBottom();
        float top = point.getIntervalTop();
        float avg = (bottom + top) / 2f;

        holder.txtIntervalBot.setText(String.format(Locale.getDefault(), "%.2f%%", bottom));
        holder.txtIntervalTop.setText(String.format(Locale.getDefault(), "%.2f%%", top));

        int colorBottom = (bottom >= 0)
                ? ContextCompat.getColor(activity, R.color.green)
                : ContextCompat.getColor(activity, R.color.red);
        int colorTop = (top >= 0)
                ? ContextCompat.getColor(activity, R.color.green)
                : ContextCompat.getColor(activity, R.color.red);

        holder.txtIntervalBot.setTextColor(colorBottom);
        holder.txtIntervalTop.setTextColor(colorTop);
    }

    public class StockViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtReturn;
        private final TextView txtDate;
        private final TextView txtIntervalBot;
        private final TextView txtIntervalTop;
        private final View confidenceIndicator;
        private final CardView parent;
        private final ImageView imgArrow;
        private final LinearLayout layoutReturnInterval;

        public StockViewHolder(View view) {
            super(view);

            txtReturn = view.findViewById(R.id.txtReturnValue);
            txtIntervalBot = view.findViewById(R.id.txtReturnIntervalBottom);
            txtIntervalTop = view.findViewById(R.id.txtReturnIntervalTop);
            txtDate = view.findViewById(R.id.txtPredictionDateShowStock);
            confidenceIndicator = view.findViewById(R.id.viewConfIndicatorShowStock);
            imgArrow = view.findViewById(R.id.imgReturnArrow);
            parent = view.findViewById(R.id.cvPredictionShowStock);
            layoutReturnInterval = view.findViewById(R.id.layoutReturnInterval);
        }
    }
}
