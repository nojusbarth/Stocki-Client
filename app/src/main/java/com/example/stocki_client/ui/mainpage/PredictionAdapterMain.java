package com.example.stocki_client.ui.mainpage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocki_client.R;
import com.example.stocki_client.prediction.PredictionSortingDataPoint;
import com.example.stocki_client.ui.stock.ShowStockActivity;

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

        holder.txtReturn.setText(String.format(Locale.getDefault(), "%.2f", point.getPctReturn()) + "%");

        if (point.getPctReturn() > 0.0) {
            holder.txtReturn.setTextColor(ContextCompat.getColor(context, R.color.green));

            holder.imgArrow.setImageResource(R.drawable.arrow_trending_up_animated);

        } else {
            holder.txtReturn.setTextColor(ContextCompat.getColor(context, R.color.red));

            holder.imgArrow.setImageResource(R.drawable.arrow_trending_down_animated);
        }

        Drawable drawable = holder.imgArrow.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }


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
        private ImageView imgArrow;
        private CardView parent;


        public StockViewHolder(View view) {
            super(view);

            imgArrow = view.findViewById(R.id.imgReturnArrowMainPage);
            txtReturn = view.findViewById(R.id.txtPredictionReturnMainPage);
            txtStockName = view.findViewById(R.id.txtStockNameMainPage);
            riskIndicator = view.findViewById(R.id.viewRiskIndicatorMainPage);
            parent = view.findViewById(R.id.cvPredictionMainPage);
        }
    }
}
