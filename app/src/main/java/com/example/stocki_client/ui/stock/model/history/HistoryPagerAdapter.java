package com.example.stocki_client.ui.stock.model.history;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class HistoryPagerAdapter extends FragmentStateAdapter {

    private final int numberOfSteps;

    public HistoryPagerAdapter(@NonNull FragmentActivity fragmentActivity, int numberOfSteps) {
        super(fragmentActivity);
        this.numberOfSteps = numberOfSteps;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return ModelHistoryFragment.newInstance(position + 1);
    }

    @Override
    public int getItemCount() {
        return numberOfSteps;
    }
}
