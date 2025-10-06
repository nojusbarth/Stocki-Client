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
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;

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

            txtLatest.setText(latestUpdate);
            txtRiskScore.setText(String.format("%s %s", getArguments().getString(ARG_RISK_SCORE), getString(R.string.label_risk_score_of)));

            btnShowHistory.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), ModelHistoryActivity.class);

                intent.putExtra("stockName", getArguments().getString(ARG_STOCK_NAME));
                intent.putExtra("interval", interval);

                getContext().startActivity(intent);
            });

        }

        return view;
    }

    private void initMetrics(View view) {
        TextView txtMAE = view.findViewById(R.id.txtMAE);
        TextView txtHitRate = view.findViewById(R.id.txtHitRate);
        TextView txtSharpe = view.findViewById(R.id.txtSharpe);
        TextView txtMaxDrawDown = view.findViewById(R.id.txtMaxDrawDown);

        txtMAE.setText(String.format("%s %s%%",getString(R.string.label_MAE), getArguments().getString(ARG_MAE)));
        txtHitRate.setText(String.format("%s %s%%", getString(R.string.label_hit_rate), getArguments().getString(ARG_HIT_RATE)));
        txtSharpe.setText(String.format("%s %s",getString(R.string.label_sharpe), getArguments().getString(ARG_SHARPE)));
        txtMaxDrawDown.setText(String.format("%s %s%%",getString(R.string.label_max_drawdown), getArguments().getString(ARG_DRAW_DOWN)));

        FrameLayout infoMaeContainer = view.findViewById(R.id.containerInfoMae);
        infoMaeContainer.setOnClickListener(v -> showInfoPopup(v,
                getString(R.string.explanation_MAE_title),
                getString(R.string.explanation_MAE_body),
                getString(R.string.explanation_MAE_example)));

        FrameLayout infoHitContainer = view.findViewById(R.id.containerInfoHitRate);
        infoHitContainer.setOnClickListener(v -> showInfoPopup(v,
                getString(R.string.explanation_hit_rate_title),
                getString(R.string.explanation_hit_rate_body),
                getString(R.string.explanation_hit_rate_example)));

        FrameLayout infoSharpeContainer = view.findViewById(R.id.containerInfoSharpe);
        infoSharpeContainer.setOnClickListener(v -> showInfoPopup(v,
                getString(R.string.explanation_sharpe_title),
                getString(R.string.explanation_sharpe_body),
                getString(R.string.explanation_sharpe_example)));

        FrameLayout infoDrawContainer = view.findViewById(R.id.containerInfoMaxDrawdown);
        infoDrawContainer.setOnClickListener(v -> showInfoPopup(v,
                getString(R.string.explanation_drawdown_title),
                getString(R.string.explanation_drawdown_body),
                getString(R.string.explanation_drawdown_example)));
    }


    private void showInfoPopup(View anchor, String title, String desc, String example) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.dialog_metric_detail, null);

        TextView txtTitle = dialogView.findViewById(R.id.txtMetricTitle);
        TextView txtDescription = dialogView.findViewById(R.id.txtMetricDescription);
        TextView txtExample = dialogView.findViewById(R.id.txtMetricExample);

        txtTitle.setText(title);
        txtDescription.setText(desc);
        txtExample.setText(example);

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setPositiveButton(getString(R.string.positive_answer), null)
                .create();

        dialog.show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
    }

}
