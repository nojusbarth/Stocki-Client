package com.example.stocki_client.ui.mainpage.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocki_client.R;
import com.example.stocki_client.data.user.favorites.FavoritesRepository;
import com.example.stocki_client.ui.ClearableAutoCompleteTextView;
import com.example.stocki_client.ui.mainpage.predictions.MainActivityViewModel;
import com.example.stocki_client.ui.mainpage.predictions.MainFragment;
import com.example.stocki_client.ui.stock.ShowStockActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private MainActivityViewModel viewModel;

    private FavoriteAdapter adapter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        initSearch(view);
        initFavorites(view);
        return view;
    }


    private void initSearch(View view) {
        ClearableAutoCompleteTextView autoSearch = view.findViewById(R.id.stockSearch);

        viewModel.getPrediction("1d").observe(getViewLifecycleOwner(), data -> {
            List<String> stockList = new ArrayList<>(data.keySet());

            ArrayAdapter<String> autoAdapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    stockList
            );

            autoSearch.setAdapter(autoAdapter);
        });

        autoSearch.setOnItemClickListener((parent, v, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);

            Intent intent = new Intent(requireContext(), ShowStockActivity.class);
            intent.putExtra("stockName", selected);
            intent.putExtra("interval", "1d");

            startActivity(intent);
        });
    }


    private void initFavorites(View view) {

        RecyclerView rvFav = view.findViewById(R.id.favRecyclerView);

        rvFav.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        TextView favHint = view.findViewById(R.id.favHint);

        adapter = new FavoriteAdapter(getContext());

        FavoritesRepository.getInstance()
                .getFavorites()
                .observe(getViewLifecycleOwner(), favoriteDisplayDataList -> {
                    if (favoriteDisplayDataList == null) return;

                    adapter.updateFavorites(favoriteDisplayDataList);

                    if (favoriteDisplayDataList.isEmpty()) {
                        favHint.setVisibility(View.VISIBLE);
                    } else {
                        favHint.setVisibility(View.GONE);
                    }
                });

        rvFav.setAdapter(adapter);

    }
}