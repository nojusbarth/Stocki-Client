package com.example.stocki_client.ui.mainpage.portfolio;

import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocki_client.R;
import com.example.stocki_client.data.user.UserIdManager;
import com.example.stocki_client.data.user.portfolio.PortfolioData;
import com.example.stocki_client.remote.ApiClient;
import com.example.stocki_client.ui.mainpage.portfolio.Listener.OnPortfolioClickListener;
import com.example.stocki_client.ui.mainpage.portfolio.dialogs.DeletePortfolioDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PortfolioAdapter extends RecyclerView.Adapter<PortfolioAdapter.PortfolioViewHolder>{



    private OnPortfolioClickListener listener;

    private List<PortfolioData> portfolios;
    private Context context;

    public PortfolioAdapter(Context context, OnPortfolioClickListener listener) {
        this.portfolios = new ArrayList<>();
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PortfolioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_portfolio_preview, parent, false);
        return new PortfolioAdapter.PortfolioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PortfolioViewHolder holder, int position) {

        Map<String, Double> portfolioMetrics = portfolios.get(position).getMetrics();

        holder.txtName.setText(portfolios.get(position).getName());
        holder.txtAbsValue.setText(String.format(Locale.getDefault(), "%.2f$",
                portfolioMetrics.get(PortfolioData.ABS_VALUE)));

        double pctChange = portfolioMetrics.get(PortfolioData.PCT_RETURN);

        holder.txtPctReturn.setText(String.format(Locale.getDefault(), "%.2f%%", pctChange));

        if (pctChange >= 0.0) {
            holder.txtPctReturn.setTextColor(ContextCompat.getColor(context, R.color.green));

            holder.imgArrow.setImageResource(R.drawable.arrow_trending_up_animated);

        } else {
            holder.txtPctReturn.setTextColor(ContextCompat.getColor(context, R.color.red));

            holder.imgArrow.setImageResource(R.drawable.arrow_trending_down_animated);
        }

        Drawable drawable = holder.imgArrow.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }


        double absChange = portfolioMetrics.get(PortfolioData.ABS_RETURN);
        holder.txtAbsReturn.setText(String.format(Locale.getDefault(), "%.2f$", absChange));

        if (absChange >= 0.0) {
            holder.txtAbsReturn.setTextColor(ContextCompat.getColor(context, R.color.green));
        } else {
            holder.txtAbsReturn.setTextColor(ContextCompat.getColor(context, R.color.red));
        }


        holder.imgDelete.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();

            new DeletePortfolioDialog(context, position1 -> {
                PortfolioData portfolio = portfolios.get(position1);

                ApiClient.getInstance().deletePortfolio(
                        UserIdManager.getInstance(context).getUserId(),
                        portfolio.getName()
                );

                portfolios.remove(position1);
                notifyItemRemoved(position1);
            }, position).show();

        });


        holder.parent.setOnClickListener(v -> {
            if (listener != null) listener.onPortfolioClick(portfolios.get(position));
        });

    }

    public void addPortfolio(PortfolioData info) {
        portfolios.add(info);

        notifyDataSetChanged();
    }

    public void setData(List<PortfolioData> info) {
        portfolios = new ArrayList<>(info);

        notifyDataSetChanged();
    }

    public void updatePortfolio(PortfolioData toUpdate) {
        for (int i = 0; i < portfolios.size(); i++) {
            PortfolioData data = portfolios.get(i);
            if (data.getName().equals(toUpdate.getName())) {
                portfolios.set(i, new PortfolioData(toUpdate));
                notifyItemChanged(i);
                return;
            }
        }
    }


    public boolean nameUsed(String name) {
        for(PortfolioData info : portfolios) {
            if(info.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return portfolios.size();
    }


    public class PortfolioViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtName;
        private final TextView txtPctReturn;
        private final TextView txtAbsReturn;
        private final TextView txtAbsValue;
        private final ImageView imgArrow;
        private final ImageView imgDelete;
        private final CardView parent;


        public PortfolioViewHolder(View view) {
            super(view);

            imgArrow = view.findViewById(R.id.imgTrendPortfolio);
            txtName = view.findViewById(R.id.txtPortfolioName);
            txtPctReturn = view.findViewById(R.id.txtPortfolioRelRet);
            txtAbsValue = view.findViewById(R.id.txtPortfolioAbsValue);
            txtAbsReturn = view.findViewById(R.id.txtPortfolioAbsRet);
            imgDelete = view.findViewById(R.id.imgDeletePortfolio);
            parent = view.findViewById(R.id.cvPortfolioPreview);

        }
    }
}
