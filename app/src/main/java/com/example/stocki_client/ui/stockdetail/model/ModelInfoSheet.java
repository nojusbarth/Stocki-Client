package com.example.stocki_client.ui.stockdetail.model;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.stocki_client.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ModelInfoSheet extends BottomSheetDialogFragment {

    private static final String ARG_LATEST = "latest";
    private static final String ARG_SAMPLES = "samples";
    private static final String ARG_METRICS = "metrics";
    private static final String ARG_RISKSCORE = "riskscore";


    public static ModelInfoSheet newInstance(String latestUpdate, String numSamples, String metrics, String riskScore) {
        ModelInfoSheet fragment = new ModelInfoSheet();
        Bundle args = new Bundle();
        args.putString(ARG_LATEST, latestUpdate);
        args.putString(ARG_SAMPLES, numSamples);
        args.putString(ARG_METRICS, metrics);
        args.putString(ARG_RISKSCORE, riskScore);
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

        if (getArguments() != null) {
            txtLatest.setText("Latest Update: " + getArguments().getString(ARG_LATEST));
            txtTrain.setText("Number samples used: " + getArguments().getString(ARG_SAMPLES));
            txtMetrics.setText("Metrics: " + getArguments().getString(ARG_METRICS));
            txtRiskScore.setText("Risk score: " + getArguments().getString(ARG_RISKSCORE) + " out of 100");
        }

        return view;
    }
}
