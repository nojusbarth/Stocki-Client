package com.example.stocki_client.ui.stock.model;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;

import com.example.stocki_client.R;
import com.example.stocki_client.TimeFormatter;
import com.example.stocki_client.ui.stock.model.history.ModelHistoryActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ModelInfoSheet extends BottomSheetDialogFragment {

    private static final String ARG_LATEST = "latest";
    private static final String ARG_CONFIDENCE = "confidence";
    private static final String ARG_STOCK_NAME = "stockname";
    private static final String ARG_INTERVAL = "interval";
    private static final String ARG_PREDICTION_TYPE = "prediction_type";
    private static final String ARG_STEP = "step";

    private static final String ARG_METRICS_MAP = "metrics_map";


    public static ModelInfoSheet newInstance(String latestUpdate,String predType, String confidence, String stockName, String interval, int step,
                                             Map<String, Double> metricsMap) {
        ModelInfoSheet fragment = new ModelInfoSheet();
        Bundle args = new Bundle();
        args.putString(ARG_LATEST, latestUpdate);
        args.putString(ARG_PREDICTION_TYPE, predType);

        args.putSerializable(ARG_METRICS_MAP, new HashMap<>(metricsMap));

        args.putString(ARG_CONFIDENCE, confidence);
        args.putString(ARG_STOCK_NAME, stockName);
        args.putString(ARG_INTERVAL, interval);
        args.putInt(ARG_STEP, step);
        fragment.setArguments(args);
        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_model_info, container, false);
        TimeFormatter timeFormatter = new TimeFormatter();

        if (getArguments() != null) {

            TextView txtLatest = view.findViewById(R.id.txtLatestUpdateValue);
            String interval = getArguments().getString(ARG_INTERVAL);
            String latestUpdate = timeFormatter.formatCV(getArguments().getString(ARG_LATEST), interval);
            txtLatest.setText(latestUpdate);

            initMetrics(view, interval, getArguments().getString(ARG_PREDICTION_TYPE));

            initConfidenceScore(view);


            Button btnShowHistory = view.findViewById(R.id.btnShowModelHistory);
            btnShowHistory.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), ModelHistoryActivity.class);

                intent.putExtra("stockName", getArguments().getString(ARG_STOCK_NAME));
                intent.putExtra("interval", interval);
                intent.putExtra("step", getArguments().getInt(ARG_STEP));

                getContext().startActivity(intent);
            });

        }

        return view;
    }

    private void initMetrics(View view, String interval, String predictionType) {

        Map<String, Double> metrics = (Map<String, Double>) getArguments().getSerializable(ARG_METRICS_MAP);

        String sharpe = String.format(Locale.getDefault(),"%.2f", metrics.get("Sharpe"));
        String drawdown = String.format(Locale.getDefault(),"%.2f", metrics.get("MaxDrawdown"));

        if (predictionType.equals("point")) {
            initMetricsPoint(view, interval, String.format(Locale.getDefault(),"%.2f", metrics.get("MAE")),
                    String.format(Locale.getDefault(),"%.2f", metrics.get("HitRate")));
        } else if (predictionType.equals("interval")) {
            initMetricsInterval(view, interval,String.format(Locale.getDefault(),"%.2f", metrics.get("Coverage")),
                    String.format(Locale.getDefault(),"%.2f", metrics.get("CWR")));
        }

        TextView txtSharpe = view.findViewById(R.id.txtSharpe);
        TextView txtMaxDrawDown = view.findViewById(R.id.txtMaxDrawDown);

        txtSharpe.setText(String.format("%s", sharpe));
        txtMaxDrawDown.setText(String.format("%s%%",drawdown));

        txtSharpe.setTextColor(MetricColorMapper.getFittingColor(sharpe,"SHARPE", interval, getContext()));
        txtMaxDrawDown.setTextColor(MetricColorMapper.getFittingColor(drawdown,"MAXDRAW", interval, getContext()));

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



    private void initMetricsPoint(View view, String interval ,String mae, String hit) {

        TextView txtMAELabel = view.findViewById(R.id.lblMetricsFirst);
        txtMAELabel.setText(getResources().getString(R.string.label_MAE));
        TextView txtHitLabel = view.findViewById(R.id.lblMetricsSecond);
        txtHitLabel.setText(getResources().getString(R.string.label_hit_rate));

        TextView txtMAE = view.findViewById(R.id.txtmetricsFirst);
        TextView txtHitRate = view.findViewById(R.id.txtMetricsSecond);

        txtMAE.setText(String.format("%s%%",mae));
        txtHitRate.setText(String.format("%s%%", hit));


        txtMAE.setTextColor(MetricColorMapper.getFittingColor(mae,"MAE", interval, getContext()));
        txtHitRate.setTextColor(MetricColorMapper.getFittingColor(hit,"HIT", interval, getContext()));


        FrameLayout infoMaeContainer = view.findViewById(R.id.containerInfoFirst);
        infoMaeContainer.setOnClickListener(v -> showInfoPopup(v,
                getString(R.string.explanation_MAE_title),
                getString(R.string.explanation_MAE_body),
                getString(R.string.explanation_MAE_example)));

        FrameLayout infoHitContainer = view.findViewById(R.id.containerInfoSecond);
        infoHitContainer.setOnClickListener(v -> showInfoPopup(v,
                getString(R.string.explanation_hit_rate_title),
                getString(R.string.explanation_hit_rate_body),
                getString(R.string.explanation_hit_rate_example)));
    }

    private void initMetricsInterval(View view, String interval, String coverage, String cwr) {

        TextView txtCoverageLabel = view.findViewById(R.id.lblMetricsFirst);
        txtCoverageLabel.setText("Coverage");
        TextView txtCWRLabel = view.findViewById(R.id.lblMetricsSecond);
        txtCWRLabel.setText("CWR");


        TextView txtCoverage = view.findViewById(R.id.txtmetricsFirst);
        TextView txtCWR = view.findViewById(R.id.txtMetricsSecond);

        txtCoverage.setText(String.format("%s%%",coverage));
        txtCWR.setText(String.format("%s", cwr));

        txtCoverage.setTextColor(MetricColorMapper.getFittingColor(coverage,"COVERAGE", interval, getContext()));
        txtCWR.setTextColor(MetricColorMapper.getFittingColor(cwr,"CWR", interval, getContext()));

        FrameLayout infoCoverageContainer = view.findViewById(R.id.containerInfoFirst);
        infoCoverageContainer.setOnClickListener(v -> showInfoPopup(v,
                getString(R.string.explanation_COVERAGE_title),
                getString(R.string.explanation_COVERAGE_body),
                getString(R.string.explanation_COVERAGE_example)));

        FrameLayout infoCWRContainer = view.findViewById(R.id.containerInfoSecond);
        infoCWRContainer.setOnClickListener(v -> showInfoPopup(v,
                getString(R.string.explanation_CWR_title),
                getString(R.string.explanation_CWR_body),
                getString(R.string.explanation_CWR_example)));


    }




    private void showInfoPopup(View anchor, String title, String desc, String example) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.dialog_metric_detail, null);

        TextView txtTitle = dialogView.findViewById(R.id.txtMetricTitle);
        TextView txtDescription = dialogView.findViewById(R.id.txtMetricDescription);
        TextView txtExample = dialogView.findViewById(R.id.txtMetricExample);

        txtTitle.setText(title);
        txtDescription.setText(Html.fromHtml(desc, Html.FROM_HTML_MODE_LEGACY));
        txtExample.setText(Html.fromHtml(example, Html.FROM_HTML_MODE_LEGACY));

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setPositiveButton(getString(R.string.positive_answer), null)
                .create();

        dialog.show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
    }


    private void initConfidenceScore(View view) {
        TextView txtConfidenceScore = view.findViewById(R.id.txtConfValue);

        String confidence = getArguments().getString(ARG_CONFIDENCE);

        float confidenceVal = 0f;
        try {
            float confidenceValue = Float.parseFloat(confidence);
            confidenceVal = confidenceValue / 100f;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            confidenceVal = 1f;
        }

        int color = ColorUtils.blendARGB(
                Color.RED,
                Color.GREEN,
                confidenceVal
        );
        txtConfidenceScore.setText(String.format("%s", confidence));
        txtConfidenceScore.setTextColor(color);

        FrameLayout infoDrawContainer = view.findViewById(R.id.containerInfoRisk);
        infoDrawContainer.setOnClickListener(v -> showInfoPopup(v,
                getString(R.string.explanation_confidence_title),
                getString(R.string.explanation_confidence_body),
                getString(R.string.explanation_confidence_example)));
    }


}
