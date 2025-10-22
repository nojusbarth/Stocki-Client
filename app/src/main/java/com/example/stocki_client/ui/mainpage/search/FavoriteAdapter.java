package com.example.stocki_client.ui.mainpage.search;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocki_client.R;
import com.example.stocki_client.data.user.favorites.FavoriteDisplayData;
import com.example.stocki_client.ui.stock.ShowStockActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>{

    private Context context;

    private List<FavoriteDisplayData> favorites = new ArrayList<>();


    public FavoriteAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_favorite, parent, false);
        return new FavoriteAdapter.FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        FavoriteDisplayData data = favorites.get(position);


        holder.txtTicker.setText(data.getTicker());

        float pctReturnDay = data.getPctReturnDay();

        holder.txtDayReturn.setText(String.format(Locale.getDefault(), "%.1f%%", pctReturnDay));

        if (pctReturnDay > 0.0f) {
            holder.txtDayReturn.setTextColor(ContextCompat.getColor(context, R.color.green));

        } else {
            holder.txtDayReturn.setTextColor(ContextCompat.getColor(context, R.color.red));
        }


        float pctReturnHour = data.getPctReturnHour();

        holder.txtHourReturn.setText(String.format(Locale.getDefault(), "%.1f%%", pctReturnHour));

        if (pctReturnHour > 0.0f) {
            holder.txtHourReturn.setTextColor(ContextCompat.getColor(context, R.color.green));

        } else {
            holder.txtHourReturn.setTextColor(ContextCompat.getColor(context, R.color.red));
        }

        holder.parent.setOnClickListener(v -> {
            Intent intent = new Intent(context, ShowStockActivity.class);

            String clickedStock = data.getTicker();

            intent.putExtra("stockName", clickedStock);
            intent.putExtra("interval", "1d");

            context.startActivity(intent);
        });

    }


    public void updateFavorites(List<FavoriteDisplayData> newFavorites) {
        this.favorites.clear();

        for (FavoriteDisplayData data : newFavorites) {
            favorites.add(data);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }


    public class FavoriteViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtTicker;
        private final TextView txtHourReturn;
        private final TextView txtDayReturn;


        private final CardView parent;


        public FavoriteViewHolder(View view) {
            super(view);

            txtTicker = view.findViewById(R.id.txtFavTickerName);
            txtHourReturn = view.findViewById(R.id.txtHourFavValue);
            txtDayReturn = view.findViewById(R.id.txtDayFavValue);
            parent = view.findViewById(R.id.cvFavorite);

        }
    }
}
