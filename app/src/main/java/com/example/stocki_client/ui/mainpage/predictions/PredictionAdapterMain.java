package com.example.stocki_client.ui.mainpage.predictions;

import android.content.Context;
import android.content.Intent;
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
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocki_client.R;
import com.example.stocki_client.data.prediction.PredictionDataPoint;
import com.example.stocki_client.ui.stock.ShowStockActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PredictionAdapterMain extends RecyclerView.Adapter<PredictionAdapterMain.StockViewHolder>{

    private List<PredictionDataPoint> predictions;
    private final String interval;
    private final Context context;

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

        PredictionDataPoint point = predictions.get(position);

        holder.txtStockName.setText(point.getTicker());

        if(point.getType().equals("point")) {
            setReturnPoint(holder, point);

        } else if (point.getType().equals("interval")) {
            setReturnInterval(holder, point);
        }

        float confidence = point.getConfidence() / 100.0f;

        int color = ColorUtils.blendARGB(
                Color.RED,
                Color.GREEN,
                confidence
        );
        holder.confidenceIndicator.getBackground().setTint(color);

        holder.parent.setOnClickListener(v -> {
            Intent intent = new Intent(context, ShowStockActivity.class);

            String clickedStock = predictions.get(holder.getAdapterPosition()).getTicker();

            intent.putExtra("stockName", clickedStock);
            intent.putExtra("interval", interval);

            context.startActivity(intent);
        });

    }

    public void updateData(List<PredictionDataPoint> newPredictions) {
        predictions = new ArrayList<>(newPredictions);

        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return predictions.size();
    }


    private void setReturnPoint(StockViewHolder holder, PredictionDataPoint point) {
        holder.txtReturn.setVisibility(View.VISIBLE);
        holder.imgArrow.setVisibility(View.VISIBLE);
        holder.layoutPredictionInterval.setVisibility(View.GONE);
        holder.txtReturn.setText(String.format(Locale.getDefault(), "%.2f%%", point.getPctReturn()));

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
    }

    private void setReturnInterval(StockViewHolder holder, PredictionDataPoint point) {
        holder.txtReturn.setVisibility(View.GONE);
        holder.imgArrow.setVisibility(View.GONE);
        holder.layoutPredictionInterval.setVisibility(View.VISIBLE);

        float bottom = point.getIntervalBottom();
        float top = point.getIntervalTop();

        holder.txtBottomInterval.setText(String.format(Locale.getDefault(), "%.2f%%", bottom));
        holder.txtTopInterval.setText(String.format(Locale.getDefault(), "%.2f%%", top));

        int colorBottom = (bottom >= 0)
                ? ContextCompat.getColor(context, R.color.green)
                : ContextCompat.getColor(context, R.color.red);
        int colorTop = (top >= 0)
                ? ContextCompat.getColor(context, R.color.green)
                : ContextCompat.getColor(context, R.color.red);

        holder.txtBottomInterval.setTextColor(colorBottom);
        holder.txtTopInterval.setTextColor(colorTop);
    }


    public class StockViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtReturn;
        private final TextView txtBottomInterval;
        private final TextView txtTopInterval;
        private final View confidenceIndicator;
        private final TextView txtStockName;
        private final ImageView imgArrow;
        private final CardView parent;
        private final LinearLayout layoutPredictionInterval;


        public StockViewHolder(View view) {
            super(view);

            imgArrow = view.findViewById(R.id.imgReturnArrowMainPage);
            txtReturn = view.findViewById(R.id.txtPredictionReturnMainPage);
            txtBottomInterval = view.findViewById(R.id.txtPredictionIntervalBottom);
            txtTopInterval = view.findViewById(R.id.txtPredictionIntervalTop);
            txtStockName = view.findViewById(R.id.txtStockNameMainPage);
            confidenceIndicator = view.findViewById(R.id.viewConfIndicatorMainPage);
            parent = view.findViewById(R.id.cvPredictionMainPage);
            layoutPredictionInterval = view.findViewById(R.id.layoutPredictionIntervalMainPage);
        }
    }
}
