package com.example.stocki_client.ui.stock.model;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.stocki_client.R;
import com.example.stocki_client.TimeFormatter;
import com.example.stocki_client.ui.stock.model.history.ModelHistoryActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ModelInfoSheet extends BottomSheetDialogFragment {

    private static final String ARG_LATEST = "latest";
    private static final String ARG_SAMPLES = "samples";
    private static final String ARG_METRICS = "metrics";
    private static final String ARG_RISKSCORE = "riskscore";
    private static final String ARG_STOCKNAME = "stockname";
    private static final String ARG_INTERVAL = "interval";

    public static ModelInfoSheet newInstance(String latestUpdate, String numSamples, String metrics, String riskScore,
                                             String stockName, String interval) {
        ModelInfoSheet fragment = new ModelInfoSheet();
        Bundle args = new Bundle();
        args.putString(ARG_LATEST, latestUpdate);
        args.putString(ARG_SAMPLES, numSamples);
        args.putString(ARG_METRICS, metrics);
        args.putString(ARG_RISKSCORE, riskScore);
        args.putString(ARG_STOCKNAME, stockName);
        args.putString(ARG_INTERVAL, interval);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_model_info, container, false);

        TextView txtLatest = view.findViewById(R.id.txtLatestUpdate);
        TextView txtTrain = view.findViewById(R.id.txtnumSamples);
        TextView txtMetrics = view.findViewById(R.id.txtMetrics);
        TextView txtRiskScore = view.findViewById(R.id.txtRiskScore);
        Button btnShowHistory = view.findViewById(R.id.btnShowModelHistory);

        TimeFormatter timeFormatter = new TimeFormatter();

        if (getArguments() != null) {

            String interval = getArguments().getString(ARG_INTERVAL);

            String latestUpdateRaw = getArguments().getString(ARG_LATEST);
            String latestUpdate = timeFormatter.formatCV(latestUpdateRaw, interval);

            txtLatest.setText("Latest Update: " + latestUpdate);
            txtTrain.setText("Number samples used: " + getArguments().getString(ARG_SAMPLES));
            txtMetrics.setText("Metrics: " + getArguments().getString(ARG_METRICS));
            txtRiskScore.setText("Risk score: " + getArguments().getString(ARG_RISKSCORE) + " out of 100");

            btnShowHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ModelHistoryActivity.class);

                    intent.putExtra("stockName", getArguments().getString(ARG_STOCKNAME));
                    intent.putExtra("interval", interval);

                    getContext().startActivity(intent);
                }
            });

        }

        return view;
    }
}
