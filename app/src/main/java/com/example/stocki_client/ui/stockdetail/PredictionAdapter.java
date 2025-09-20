package com.example.stocki_client.ui.stockdetail;

import android.content.Context;
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
import com.example.stocki_client.prediction.PredictionDataPoint;
import com.example.stocki_client.remote.ApiClient;
import com.example.stocki_client.remote.DataCallback;
import com.example.stocki_client.ui.StockAdapter;
import com.example.stocki_client.ui.stockdetail.model.ModelInfo;
import com.example.stocki_client.ui.stockdetail.model.ModelInfoSheet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PredictionAdapter extends RecyclerView.Adapter<PredictionAdapter.StockViewHolder>{

    private List<PredictionDataPoint> predictions;
    private String ticker;
    private String interval;
    private AppCompatActivity activity;

    public PredictionAdapter(String ticker, String interval, AppCompatActivity activity) {
        predictions = new ArrayList<>();
        this.ticker = ticker;
        this.interval = interval;
        this.activity = activity;
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
                ApiClient.getInstance().getModelInfo(ticker, interval, new DataCallback<ModelInfo>() {
                    @Override
                    public void onSuccess(ModelInfo modelInfo) {

                        Map<String, Double> metrics = modelInfo.getMetrics();
                        StringBuilder sb = new StringBuilder();
                        for (Map.Entry<String, Double> entry : metrics.entrySet()) {
                            sb.append(entry.getKey())
                                    .append(": ")
                                    .append(String.format("%.2f", entry.getValue()))
                                    .append("   ");
                        }
                        String metricsString = sb.toString();


                        ModelInfoSheet sheet = ModelInfoSheet.newInstance(
                                modelInfo.getLatestUpdate(),
                                String.valueOf(modelInfo.getNumSamples()),
                                metricsString,
                                String.valueOf(point.getRiskScore())
                        );
                        sheet.show(activity.getSupportFragmentManager(), "ModelInfoBottomSheet");
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        activity.runOnUiThread(() ->
                                Toast.makeText(activity, "Fehler beim Laden", Toast.LENGTH_SHORT).show()
                        );
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

            txtReturn = view.findViewById(R.id.txtPredictionReturn);
            txtDate = view.findViewById(R.id.txtPredictionDate);
            riskIndicator = view.findViewById(R.id.viewRiskIndicator);
            parent = view.findViewById(R.id.cvPrediction);
        }
    }
}
