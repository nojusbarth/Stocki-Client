package com.example.stocki_client.ui.stock.model.history;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocki_client.R;
import com.example.stocki_client.data.prediction.AccuracyDataPoint;
import com.example.stocki_client.data.prediction.DatedAccuracy;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ModelHistoryAdapter extends RecyclerView.Adapter<ModelHistoryAdapter.AccuracyViewHolder>{


    private List<DatedAccuracy> data;
    private final int step;
    private Context context;

    public ModelHistoryAdapter(Context context, int step) {
        this.step = step-1;
        this.context = context;

        data = new ArrayList<>();
    }

    public void updateData(Map<String, List<AccuracyDataPoint>> newData, String interval) {
        data.clear();
        data = new ArrayList<>(DatedAccuracy.extractStepData(newData, step, interval));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AccuracyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_accuracy, parent, false);
        return new ModelHistoryAdapter.AccuracyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccuracyViewHolder holder, int position) {

        holder.parent.setOnClickListener(v -> {
            holder.showAbsolute = !holder.showAbsolute;
            notifyDataSetChanged();
        });

        DatedAccuracy accuracy = data.get(position);

        holder.txtDate.setText(accuracy.getDate());

        AccuracyDataPoint dataPoint = accuracy.getData();

        if (dataPoint.getPredType().equals("point")) {
            initCardPoint(holder, dataPoint);
        } else if (dataPoint.getPredType().equals("interval")) {
            initCardInterval(holder, dataPoint);
        }

        holder.txtConfidence.setText(String.valueOf(dataPoint.getConfidence()));
        float confidence = dataPoint.getConfidence() / 100f;
        int color = ColorUtils.blendARGB(
                Color.RED,
                Color.GREEN,
                confidence
        );
        holder.txtConfidence.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    private void initCardPoint(AccuracyViewHolder holder, AccuracyDataPoint dataPoint) {

        holder.intervalLayout.setVisibility(View.GONE);
        holder.txtPredicted.setVisibility(View.VISIBLE);
        holder.txtDifferenceLabel.setText(context.getResources().getString(R.string.label_difference));

        if (holder.showAbsolute) {
            String predicted = String.format(Locale.getDefault(), "%.2f", dataPoint.getClosePrediction());
            String actual = String.format(Locale.getDefault(), "%.2f", dataPoint.getActualClose());
            float diff = dataPoint.getClosePrediction() - dataPoint.getActualClose();

            holder.txtPredicted.setText(String.format(Locale.getDefault(),"%s$", predicted));
            holder.txtActual.setText(String.format(Locale.getDefault(),"%s$", actual));
            holder.txtDifference.setText(String.format(Locale.getDefault(), "%.2f$", Math.abs(diff)));
        } else {
            String predictedPct = String.format(Locale.getDefault(), "%.2f%%", dataPoint.getPctReturnPrediction());
            float actualPctVal = ((dataPoint.getActualClose() - (dataPoint.getClosePrediction() / (1 + dataPoint.getPctReturnPrediction() / 100f)))
                    / (dataPoint.getClosePrediction() / (1 + dataPoint.getPctReturnPrediction() / 100f))) * 100f;

            float diffPctVal = actualPctVal - dataPoint.getPctReturnPrediction();

            String actualPct = String.format(Locale.getDefault(), "%.2f%%", actualPctVal);
            String diffPct = String.format(Locale.getDefault(), "%.2f%%", Math.abs(diffPctVal));

            holder.txtPredicted.setText(predictedPct);
            if (dataPoint.getPctReturnPrediction() > 0.0) {
                holder.txtPredicted.setTextColor(ContextCompat.getColor(context, R.color.green));
            } else {
                holder.txtPredicted.setTextColor(ContextCompat.getColor(context, R.color.red));
            }

            holder.txtActual.setText(actualPct);
            if (actualPctVal > 0.0) {
                holder.txtActual.setTextColor(ContextCompat.getColor(context, R.color.green));
            } else {
                holder.txtActual.setTextColor(ContextCompat.getColor(context, R.color.red));
            }


            holder.txtDifference.setText(diffPct);
        }
    }

    private void initCardInterval(AccuracyViewHolder holder, AccuracyDataPoint dataPoint) {

        holder.intervalLayout.setVisibility(View.VISIBLE);
        holder.txtPredicted.setVisibility(View.GONE);
        holder.txtDifferenceLabel.setText("Coverage");


        float bottom = dataPoint.getIntervalBottom();
        float top = dataPoint.getIntervalTop();

        holder.txtPredictedBot.setText(String.format(Locale.getDefault(), "%.2f%%", bottom));
        holder.txtPredictedTop.setText(String.format(Locale.getDefault(), "%.2f%%", top));

        int colorBottom = (bottom >= 0)
                ? ContextCompat.getColor(context, R.color.green)
                : ContextCompat.getColor(context, R.color.red);
        int colorTop = (top >= 0)
                ? ContextCompat.getColor(context, R.color.green)
                : ContextCompat.getColor(context, R.color.red);

        holder.txtPredictedBot.setTextColor(colorBottom);
        holder.txtPredictedTop.setTextColor(colorTop);


        if (dataPoint.getActualClose() >= bottom && dataPoint.getActualClose() <= top) {
            holder.txtDifference.setText("inside");
            holder.txtDifference.setTextColor(ContextCompat.getColor(context, R.color.green));
        } else {
            holder.txtDifference.setText("outside");
            holder.txtDifference.setTextColor(ContextCompat.getColor(context, R.color.red));
        }

    }


    public class AccuracyViewHolder extends RecyclerView.ViewHolder {

        private final CardView parent;
        private final TextView txtDate;
        private final TextView txtPredicted;
        private final TextView txtPredictedTop;
        private final TextView txtPredictedBot;
        private final TextView txtActual;
        private final TextView txtDifference;
        private final TextView txtDifferenceLabel;
        private final TextView txtConfidence;
        private final LinearLayout intervalLayout;
        private boolean showAbsolute = false;

        public AccuracyViewHolder(View view) {
            super(view);

            parent = view.findViewById(R.id.cvAccuracy);
            txtDate = view.findViewById(R.id.txtAccuracyDate);
            txtPredicted = view.findViewById(R.id.txtPredictedValue);
            txtPredictedTop = view.findViewById(R.id.txtIntervalTopHistory);
            txtPredictedBot = view.findViewById(R.id.txtIntervalBottomHistory);
            txtActual = view.findViewById(R.id.txtActualValue);
            txtDifference = view.findViewById(R.id.txtDifferenceValue);
            txtDifferenceLabel = view.findViewById(R.id.txtDifferenceLabel);
            txtConfidence = view.findViewById(R.id.txtConfScoreValue);
            intervalLayout = view.findViewById(R.id.layoutIntervalPredictionHistory);
        }
    }
}
