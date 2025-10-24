package com.example.stocki_client.ui.mainpage.predictions;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.stocki_client.R;
import com.example.stocki_client.ui.ClearableAutoCompleteTextView;
import com.example.stocki_client.ui.settings.AboutActivity;
import com.example.stocki_client.ui.stock.ShowStockActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;


public class MainFragment extends Fragment {

    private MainActivityViewModel viewModel;


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

        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        initPager(view);
        initAbout(view);

        return view;
    }


    private void initPager(View view) {
        TabLayout tabLayoutPredictions = view.findViewById(R.id.tabLayoutPredictions);
        ViewPager2 viewPagerPredictions = view.findViewById(R.id.viewPagerPredictions);

        PredictionPagerAdapter pagerAdapter = new PredictionPagerAdapter(requireActivity());
        viewPagerPredictions.setAdapter(pagerAdapter);

        viewPagerPredictions.setUserInputEnabled(false);

        new TabLayoutMediator(tabLayoutPredictions, viewPagerPredictions,
                (tab, position) -> {
                    if (position == PredictionPagerAdapter.POSITION_DAILY) {
                        tab.setText(getResources().getString(R.string.button_daily_text));
                    } else if (position == PredictionPagerAdapter.POSITION_HOURLY){
                        tab.setText(getResources().getString(R.string.button_hourly_text));
                    } else if(position == PredictionPagerAdapter.POSITION_TWO_WEEK) {
                        tab.setText("2 Weeks");
                    }
                }
        ).attach();
    }

    private void initAbout(View view) {
        Button aboutButton = view.findViewById(R.id.buttonAbout);

        aboutButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AboutActivity.class);
            startActivity(intent);
        });
    }
}


