package com.example.stocki_client.ui.stock.model.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocki_client.R;
import com.example.stocki_client.prediction.AccuracyDataPoint;
import com.example.stocki_client.prediction.DatedAccuracy;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ModelHistoryAdapter extends RecyclerView.Adapter<ModelHistoryAdapter.AccuracyViewHolder>{


    private List<DatedAccuracy> data;
    private int step;

    public ModelHistoryAdapter(int step) {
        this.step = step-1;

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

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.showAbsolute = !holder.showAbsolute;
                notifyDataSetChanged();
            }
        });

        DatedAccuracy accuracy = data.get(position);

        holder.txtDate.setText(accuracy.getDate());

        AccuracyDataPoint dataPoint = accuracy.getData();

        if (holder.showAbsolute) {
            String predicted = String.format(Locale.getDefault(), "%.2f", dataPoint.getClosePrediction());
            String actual = String.format(Locale.getDefault(), "%.2f", dataPoint.getActualClose());
            float diff = dataPoint.getClosePrediction() - dataPoint.getActualClose();

            holder.txtPredicted.setText(String.format(Locale.getDefault(),"Predicted: %s$", predicted));
            holder.txtActual.setText(String.format(Locale.getDefault(),"Actual: %s$", actual));
            holder.txtDifference.setText(String.format(Locale.getDefault(), "Diff: %.2f$", Math.abs(diff)));
        } else {
            String predictedPct = String.format(Locale.getDefault(), "%.2f%%", dataPoint.getPctReturnPrediction());
            float actualPctVal = ((dataPoint.getActualClose() - (dataPoint.getClosePrediction() / (1 + dataPoint.getPctReturnPrediction() / 100f)))
                    / (dataPoint.getClosePrediction() / (1 + dataPoint.getPctReturnPrediction() / 100f))) * 100f;

            float diffPctVal = actualPctVal - dataPoint.getPctReturnPrediction();

            String actualPct = String.format(Locale.getDefault(), "%.2f%%", actualPctVal);
            String diffPct = String.format(Locale.getDefault(), "%.2f%%", Math.abs(diffPctVal));

            holder.txtPredicted.setText("Predicted: " + predictedPct);
            holder.txtActual.setText("Actual: " + actualPct);
            holder.txtDifference.setText("Diff: " + diffPct);
        }

        holder.txtRiskScore.setText("Risk Score was: " + dataPoint.getRiskPrediction());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    public class AccuracyViewHolder extends RecyclerView.ViewHolder {

        private CardView parent;
        private TextView txtDate;
        private TextView txtPredicted;
        private TextView txtActual;
        private TextView txtDifference;
        private TextView txtRiskScore;
        private boolean showAbsolute = false;

        public AccuracyViewHolder(View view) {
            super(view);

            parent = view.findViewById(R.id.cvAccuracy);
            txtDate = view.findViewById(R.id.txtAccuracyDate);
            txtPredicted = view.findViewById(R.id.txtAccuracyPredicted);
            txtActual = view.findViewById(R.id.txtAccuracyActual);
            txtDifference = view.findViewById(R.id.txtAccuracyDifference);
            txtRiskScore = view.findViewById(R.id.txtAccuracyRiskScore);
        }
    }
}
