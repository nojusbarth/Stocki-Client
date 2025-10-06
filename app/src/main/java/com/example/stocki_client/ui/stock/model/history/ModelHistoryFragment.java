package com.example.stocki_client.ui.stock.model.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.stocki_client.R;

import com.github.mikephil.charting.charts.LineChart;

import java.util.HashMap;

public class ModelHistoryFragment extends Fragment {

    private static final String ARG_STEP = "step";
    private int step;
    private String interval;
    private ModelHistoryViewModel viewModel;
    private AccuracyChartBuilder chartBuilder;


    public static ModelHistoryFragment newInstance(int step) {
        ModelHistoryFragment fragment = new ModelHistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_STEP, step);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            step = getArguments().getInt(ARG_STEP);
            viewModel = new ViewModelProvider(requireActivity()).get(ModelHistoryViewModel.class);
            interval = viewModel.getInterval();
            chartBuilder = new AccuracyChartBuilder(interval, step);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_predictions_history, container, false);

        TextView txtTitle = view.findViewById(R.id.txtAccuracyStepTitleValue);
        txtTitle.setText(String.valueOf(step));


        RecyclerView recView = view.findViewById(R.id.recPredictionsHistory);
        recView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));

        ModelHistoryAdapter historyAdapter = new ModelHistoryAdapter(this.step);

        recView.setAdapter(historyAdapter);

        viewModel.getAccuracy().observe(getViewLifecycleOwner(), data -> {

            chartBuilder.setData(new HashMap<>(data));
            LineChart shell = view.findViewById(R.id.stockDataChartHistory);
            chartBuilder.buildChart(shell);
            historyAdapter.updateData(new HashMap<>(data), interval);
        });


        return view;
    }

}
