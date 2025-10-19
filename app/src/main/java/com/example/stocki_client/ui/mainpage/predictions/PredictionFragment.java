package com.example.stocki_client.ui.mainpage.predictions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocki_client.R;
import com.example.stocki_client.prediction.PredictionSorter;

import java.util.HashMap;


public class PredictionFragment extends Fragment {

    private static final String ARG_INTERVAL = "interval";
    private String interval;
    private MainActivityViewModel viewModel;


    public static PredictionFragment newInstance(String interval) {
        PredictionFragment fragment = new PredictionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_INTERVAL, interval);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        if (getArguments() != null) {
            interval = getArguments().getString(ARG_INTERVAL);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_predictions_mainpage, container, false);

        RecyclerView recyclerWinners = view.findViewById(R.id.recyclerWinners);
        RecyclerView recyclerLosers = view.findViewById(R.id.recyclerLosers);

        recyclerWinners.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerLosers.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        PredictionAdapterMain winnersAdapter = new PredictionAdapterMain(interval, getActivity());
        PredictionAdapterMain losersAdapter = new PredictionAdapterMain(interval, getActivity());

        recyclerWinners.setAdapter(winnersAdapter);
        recyclerLosers.setAdapter(losersAdapter);

        viewModel.getPrediction(interval).observe(getViewLifecycleOwner(), data -> {
            PredictionSorter predictionSorter = new PredictionSorter();
            predictionSorter.setData(new HashMap<>(data));

            winnersAdapter.updateData(predictionSorter.getWinners());
            losersAdapter.updateData(predictionSorter.getLosers());

        });


        return view;
    }
}
