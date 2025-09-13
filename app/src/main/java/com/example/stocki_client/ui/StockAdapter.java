package com.example.stocki_client.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocki_client.R;
import com.example.stocki_client.ui.stockdetail.ShowStockActivity;

import java.util.ArrayList;
import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {

    private List<String> stockList;
    private List<String> filteredList;

    private Context context;

    public StockAdapter(Context context) {
        this.stockList = new ArrayList<>();
        this.filteredList = new ArrayList<>(stockList);

        this.context = context;

        notifyDataSetChanged();
    }


    public void updateData(List<String> stockNames) {
        stockList.clear();
        stockList.addAll(stockNames);
        filteredList = new ArrayList<>(stockList);
        notifyDataSetChanged();
    }





    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_stock, parent, false);
        return new StockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        holder.txtStockName.setText(filteredList.get(position));

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ShowStockActivity.class);

                String clickedStock = filteredList.get(holder.getAdapterPosition());

                intent.putExtra("stockName", clickedStock);

                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }


    public void filter(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(stockList);
        } else {
            for (String stock : stockList) {
                if (stock.toLowerCase().startsWith(query.toLowerCase())) {
                    filteredList.add(stock);
                }
            }
        }
        notifyDataSetChanged();
    }



    public class StockViewHolder extends RecyclerView.ViewHolder {

        private CardView parent;
        private TextView txtStockName;

        public StockViewHolder(View view) {
            super(view);

            txtStockName = view.findViewById(R.id.cardviewstockname);
            parent = view.findViewById(R.id.cardviewstockparent);

        }
    }
}
