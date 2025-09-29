package com.example.stocki_client.ui.stock.model;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.stocki_client.R;
import com.example.stocki_client.TimeFormatter;
import com.example.stocki_client.ui.stock.model.history.ModelHistoryActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ModelInfoSheet extends BottomSheetDialogFragment {

    private static final String ARG_LATEST = "latest";
    private static final String ARG_MAE = "mae";
    private static final String ARG_HIT_RATE = "hitrate";
    private static final String ARG_SHARPE = "sharpe";
    private static final String ARG_DRAW_DOWN = "drawdown";
    private static final String ARG_RISK_SCORE = "riskscore";
    private static final String ARG_STOCK_NAME = "stockname";
    private static final String ARG_INTERVAL = "interval";

    public static ModelInfoSheet newInstance(String latestUpdate, String MAE, String hitRate,
            String sharpe, String drawDown, String riskScore, String stockName, String interval) {
        ModelInfoSheet fragment = new ModelInfoSheet();
        Bundle args = new Bundle();
        args.putString(ARG_LATEST, latestUpdate);
        args.putString(ARG_MAE, MAE);
        args.putString(ARG_HIT_RATE, hitRate);
        args.putString(ARG_SHARPE, sharpe);
        args.putString(ARG_DRAW_DOWN, drawDown);

        args.putString(ARG_RISK_SCORE, riskScore);
        args.putString(ARG_STOCK_NAME, stockName);
        args.putString(ARG_INTERVAL, interval);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_model_info, container, false);

        TextView txtLatest = view.findViewById(R.id.txtLatestUpdateValue);
        TextView txtRiskScore = view.findViewById(R.id.txtRiskValue);
        Button btnShowHistory = view.findViewById(R.id.btnShowModelHistory);

        TimeFormatter timeFormatter = new TimeFormatter();

        if (getArguments() != null) {

            String interval = getArguments().getString(ARG_INTERVAL);

            String latestUpdateRaw = getArguments().getString(ARG_LATEST);
            initMetrics(view);
            String latestUpdate = timeFormatter.formatCV(latestUpdateRaw, interval);

            txtLatest.setText(String.format("Latest Update: %s", latestUpdate));
            txtRiskScore.setText(String.format("Risk score: %s out of 100 ", getArguments().getString(ARG_RISK_SCORE)));

            btnShowHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ModelHistoryActivity.class);

                    intent.putExtra("stockName", getArguments().getString(ARG_STOCK_NAME));
                    intent.putExtra("interval", interval);

                    getContext().startActivity(intent);
                }
            });

        }

        return view;
    }

    private void initMetrics(View view) {
        TextView txtMAE = view.findViewById(R.id.txtMAE);
        TextView txtHitRate = view.findViewById(R.id.txtHitRate);
        TextView txtSharpe = view.findViewById(R.id.txtSharpe);
        TextView txtMaxDrawDown = view.findViewById(R.id.txtMaxDrawDown);

        txtMAE.setText(String.format("MAE: %s%%", getArguments().getString(ARG_MAE)));
        txtHitRate.setText(String.format("Hit Rate: %s%%", getArguments().getString(ARG_HIT_RATE)));
        txtSharpe.setText(String.format("Sharpe Ratio: %s", getArguments().getString(ARG_SHARPE)));
        txtMaxDrawDown.setText(String.format("Maximum Drawdown: %s%%", getArguments().getString(ARG_DRAW_DOWN)));

        FrameLayout infoMaeContainer = view.findViewById(R.id.containerInfoMae);
        infoMaeContainer.setOnClickListener(v -> {
            showInfoPopup(v, "MAE = Mean Absolute Error\nMisst die durchschnittliche Abweichung.",
                    "MAE explanation");
        });

        FrameLayout infoHitContainer = view.findViewById(R.id.containerInfoHitRate);
        infoHitContainer.setOnClickListener(v -> {
            showInfoPopup(v, "Zuverlaessigkeit",
                    "Hit Rate explanation");
        });

        FrameLayout infoSharpeContainer = view.findViewById(R.id.containerInfoSharpe);
        infoSharpeContainer.setOnClickListener(v -> {
            showInfoPopup(v, "genauikgeit",
                    "Sharpe Ratio explanation");
        });

        FrameLayout infoDrawContainer = view.findViewById(R.id.containerInfoMaxDrawdown);
        infoDrawContainer.setOnClickListener(v -> {
            showInfoPopup(v, "verlust",
                    "Max Drawdown explanation");
        });
    }


    private void showInfoPopup(View anchor, String message, String title) {
        new AlertDialog.Builder(getContext(), R.style.MyAlertDialogTheme)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

}
