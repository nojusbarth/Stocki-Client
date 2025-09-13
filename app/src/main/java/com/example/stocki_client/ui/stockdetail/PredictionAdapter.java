package com.example.stocki_client.ui.stockdetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocki_client.R;
import com.example.stocki_client.prediction.PredictionDataPoint;
import com.example.stocki_client.ui.StockAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PredictionAdapter extends RecyclerView.Adapter<PredictionAdapter.StockViewHolder>{

    private List<PredictionDataPoint> predictions;

    public PredictionAdapter() {
        predictions = new ArrayList<>();
    }

    @NonNull
    @Override
    public PredictionAdapter.StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_prediction, parent, false);
        return new PredictionAdapter.StockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PredictionAdapter.StockViewHolder holder, int position) {
        PredictionDataPoint point = predictions.get(position);

        String rawDate = point.getDate();
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

        Date date = null;
        try {
            date = inputFormat.parse(rawDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        String formatted = outputFormat.format(date);
        holder.txtDate.setText(formatted);
        holder.txtReturn.setText("Return: " + String.format(Locale.getDefault(), "%.2f", point.getPctReturn()) + "%");
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


    public class StockViewHolder extends RecyclerView.ViewHolder {

        private TextView txtReturn;
        private TextView txtRisk;
        private TextView txtDate;

        public StockViewHolder(View view) {
            super(view);

            txtReturn = view.findViewById(R.id.txtPredictionReturn);
            txtRisk = view.findViewById(R.id.txtPredictionRisk);
            txtDate = view.findViewById(R.id.txtPredictionDate);


        }
    }
}
