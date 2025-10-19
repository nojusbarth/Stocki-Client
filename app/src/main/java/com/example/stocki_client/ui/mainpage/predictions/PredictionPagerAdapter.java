package com.example.stocki_client.ui.mainpage.predictions;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PredictionPagerAdapter extends FragmentStateAdapter {

    public static final int POSITION_DAILY = 0;
    public static final int POSITION_HOURLY = 1;


    public PredictionPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == POSITION_DAILY) {
            return PredictionFragment.newInstance("1d");
        } else if(position == POSITION_HOURLY) {
            return PredictionFragment.newInstance("1h");
        } else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
