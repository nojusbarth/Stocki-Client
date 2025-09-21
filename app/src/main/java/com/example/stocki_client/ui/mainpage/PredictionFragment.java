package com.example.stocki_client.ui.mainpage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocki_client.R;
import com.example.stocki_client.prediction.PredictionDataPoint;
import com.example.stocki_client.prediction.PredictionSorter;
import com.example.stocki_client.remote.ApiClient;
import com.example.stocki_client.remote.DataCallback;

import java.util.Map;

public class PredictionFragment extends Fragment {

    private static final String ARG_INTERVAL = "interval";
    private String interval;

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


        ApiClient.getInstance().getAllPredictions(interval, new DataCallback<Map<String, PredictionDataPoint>>() {
            @Override
            public void onSuccess(Map<String, PredictionDataPoint> data) {
                PredictionSorter predictionSorter = new PredictionSorter();
                predictionSorter.setData(data);
                getActivity().runOnUiThread(() -> {
                    winnersAdapter.updateData(predictionSorter.getWinners());
                    losersAdapter.updateData(predictionSorter.getLosers());
                });
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireActivity(), "Fehler beim Laden", Toast.LENGTH_SHORT).show()
                );
            }
        });

        return view;
    }
}
